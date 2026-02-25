package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Comodidade;
import com.bemystay.be_my_stay.model.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    @Query("""
    SELECT r FROM Reserva r
    WHERE r.imovel.id = :id_imovel
    AND r.ativo = true
      AND (
            :checkin <= r.checkout
        AND :checkout >= r.checkin
      )
""")
    List<Reserva> buscarConflitos(
            @Param("id_imovel") Long id,
            @Param("checkin") LocalDate checkin,
            @Param("checkout") LocalDate checkout
    );

    @Query("""
    SELECT r FROM Reserva r
    WHERE r.imovel.id = :id_imovel
      AND r.ativo = true
""")
    List<Reserva> buscarReservasAtivas(@Param("id_imovel") Long id);


    @Query("SELECT r FROM Reserva r WHERE r.ativo = true")
    List<Reserva> findAtivos();

    @Query("SELECT r FROM Reserva r WHERE r.ativo = false")
    List<Reserva> findInativos();

    List<Reserva> findByUsuarioIdAndAtivoTrueAndCheckoutGreaterThanEqual(
            Long usuarioId,
            LocalDate dataAtual
    );

    List<Reserva> findByUsuarioIdAndAtivoTrueAndCheckoutLessThan(
            Long usuarioId,
            LocalDate dataAtual
    );

    @Query("""
       SELECT r.imovel.titulo,
              COUNT(r),
              SUM(r.total)
       FROM Reserva r
       WHERE r.checkout < :hoje
       AND r.ativo = true
       GROUP BY r.imovel.titulo
       ORDER BY SUM(r.total) DESC
       """)
    List<Object[]> buscarImoveisMaisLucrativos(@Param("hoje") LocalDate hoje);

    @Query("""
   SELECT r.usuario.email,
          COUNT(r)
   FROM Reserva r
   WHERE r.ativo = true
   GROUP BY r.usuario.email
   ORDER BY COUNT(r) DESC
   """)
    List<Object[]> usuariosComMaisReservas();

    @Query("""
       SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
       FROM Reserva r
       WHERE r.imovel.id = :imovelId
       AND :checkin < r.checkout
       AND :checkout > r.checkin
       """)
    boolean existeReservaConflitante(
            Long imovelId,
            LocalDate checkin,
            LocalDate checkout
    );

    @Query("""
       SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
       FROM Reserva r
       WHERE r.imovel.id = :imovelId
       AND r.ativo = true
       AND r.checkout >= :hoje
       """)
    boolean existeReservaFutura(@Param("imovelId") Long imovelId,
                                @Param("hoje") LocalDate hoje);
}
