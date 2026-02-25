package com.bemystay.be_my_stay.repository;

import com.bemystay.be_my_stay.model.Imovel;
import com.bemystay.be_my_stay.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ImovelRepository extends JpaRepository <Imovel, Long> {
    @Query("SELECT i FROM Imovel i WHERE i.ativo = true")
    List<Imovel> findByAtivos();

    @Query("SELECT i FROM Imovel  i WHERE i.ativo = false ")
    List<Imovel> findByInativos();

    boolean existsByUsuario(Usuario usuario);

    long countByAtivoTrue();

    long countByAtivoFalse();

    List<Imovel> findByTipoImovelIdAndAtivoTrue(Long id);

    List<Imovel> findByUsuarioIdAndAtivoTrue(Long id);

    List<Imovel> findByUsuarioIdAndAtivoFalse(Long idUsuario);

    List<Imovel> findByEndereco_CidadeContainingIgnoreCaseAndAtivoTrue(String cidade);

    @Query("""
       SELECT i
       FROM Imovel i
       WHERE i.dataCadastro BETWEEN :inicio AND :fim
       ORDER BY i.dataCadastro DESC
       """)
    List<Imovel> buscarImoveisUltimoMes(
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim);


}
