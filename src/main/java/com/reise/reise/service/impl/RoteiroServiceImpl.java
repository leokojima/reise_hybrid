package com.reise.reise.service.impl;

import com.reise.reise.domain.Roteiro;
import com.reise.reise.repository.RoteiroRepository;
import com.reise.reise.service.RoteiroService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Roteiro}.
 */
@Service
@Transactional
public class RoteiroServiceImpl implements RoteiroService {

    private final Logger log = LoggerFactory.getLogger(RoteiroServiceImpl.class);

    private final RoteiroRepository roteiroRepository;

    public RoteiroServiceImpl(RoteiroRepository roteiroRepository) {
        this.roteiroRepository = roteiroRepository;
    }

    @Override
    public Roteiro save(Roteiro roteiro) {
        log.debug("Request to save Roteiro : {}", roteiro);
        return roteiroRepository.save(roteiro);
    }

    @Override
    public Optional<Roteiro> partialUpdate(Roteiro roteiro) {
        log.debug("Request to partially update Roteiro : {}", roteiro);

        return roteiroRepository
            .findById(roteiro.getId())
            .map(
                existingRoteiro -> {
                    if (roteiro.getNomeroteiro() != null) {
                        existingRoteiro.setNomeroteiro(roteiro.getNomeroteiro());
                    }
                    if (roteiro.getTipo() != null) {
                        existingRoteiro.setTipo(roteiro.getTipo());
                    }
                    if (roteiro.getStatusr() != null) {
                        existingRoteiro.setStatusr(roteiro.getStatusr());
                    }

                    return existingRoteiro;
                }
            )
            .map(roteiroRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Roteiro> findAll(Pageable pageable) {
        log.debug("Request to get all Roteiros");
        return roteiroRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Roteiro> findOne(Long id) {
        log.debug("Request to get Roteiro : {}", id);
        return roteiroRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Roteiro : {}", id);
        roteiroRepository.deleteById(id);
    }
}
