package com.bemystay.be_my_stay.repository;

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

    @Query("""
    SELECT r FROM Reserva r
    WHERE r.imovel.id = :idImovel
      AND r.ativo = true
      AND r.usuario.id = :idUsuario
""")
    List<Reserva> buscarReservasDoUsuario(
            @Param("idImovel") Long idImovel,
            @Param("idUsuario") Long idUsuario
    );
    @Query("""
    SELECT r FROM Reserva r
    WHERE r.usuario.id = :idUsuario
      AND r.ativo = true
""")
    List<Reserva> buscarPorUsuario(@Param("idUsuario") Long idUsuario);

}
