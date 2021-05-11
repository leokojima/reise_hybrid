package com.reise.reise.service.impl;

import com.reise.reise.domain.Compartilhamento;
import com.reise.reise.repository.CompartilhamentoRepository;
import com.reise.reise.service.CompartilhamentoService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Compartilhamento}.
 */
@Service
@Transactional
public class CompartilhamentoServiceImpl implements CompartilhamentoService {

    private final Logger log = LoggerFactory.getLogger(CompartilhamentoServiceImpl.class);

    private final CompartilhamentoRepository compartilhamentoRepository;

    public CompartilhamentoServiceImpl(CompartilhamentoRepository compartilhamentoRepository) {
        this.compartilhamentoRepository = compartilhamentoRepository;
    }

    @Override
    public Compartilhamento save(Compartilhamento compartilhamento) {
        log.debug("Request to save Compartilhamento : {}", compartilhamento);
        return compartilhamentoRepository.save(compartilhamento);
    }

    @Override
    public Optional<Compartilhamento> partialUpdate(Compartilhamento compartilhamento) {
        log.debug("Request to partially update Compartilhamento : {}", compartilhamento);

        return compartilhamentoRepository
            .findById(compartilhamento.getId())
            .map(
                existingCompartilhamento -> {
                    if (compartilhamento.getNomeComp() != null) {
                        existingCompartilhamento.setNomeComp(compartilhamento.getNomeComp());
                    }
                    if (compartilhamento.getDescricaoComp() != null) {
                        existingCompartilhamento.setDescricaoComp(compartilhamento.getDescricaoComp());
                    }
                    if (compartilhamento.getDataCriacao() != null) {
                        existingCompartilhamento.setDataCriacao(compartilhamento.getDataCriacao());
                    }

                    return existingCompartilhamento;
                }
            )
            .map(compartilhamentoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Compartilhamento> findAll() {
        log.debug("Request to get all Compartilhamentos");
        return compartilhamentoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Compartilhamento> findOne(Long id) {
        log.debug("Request to get Compartilhamento : {}", id);
        return compartilhamentoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Compartilhamento : {}", id);
        compartilhamentoRepository.deleteById(id);
    }
}
