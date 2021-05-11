package com.reise.reise.service.impl;

import com.reise.reise.domain.Viajante;
import com.reise.reise.repository.ViajanteRepository;
import com.reise.reise.service.ViajanteService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Viajante}.
 */
@Service
@Transactional
public class ViajanteServiceImpl implements ViajanteService {

    private final Logger log = LoggerFactory.getLogger(ViajanteServiceImpl.class);

    private final ViajanteRepository viajanteRepository;

    public ViajanteServiceImpl(ViajanteRepository viajanteRepository) {
        this.viajanteRepository = viajanteRepository;
    }

    @Override
    public Viajante save(Viajante viajante) {
        log.debug("Request to save Viajante : {}", viajante);
        return viajanteRepository.save(viajante);
    }

    @Override
    public Optional<Viajante> partialUpdate(Viajante viajante) {
        log.debug("Request to partially update Viajante : {}", viajante);

        return viajanteRepository
            .findById(viajante.getId())
            .map(
                existingViajante -> {
                    if (viajante.getNome() != null) {
                        existingViajante.setNome(viajante.getNome());
                    }
                    if (viajante.getEmail() != null) {
                        existingViajante.setEmail(viajante.getEmail());
                    }
                    if (viajante.getFoto() != null) {
                        existingViajante.setFoto(viajante.getFoto());
                    }
                    if (viajante.getFotoContentType() != null) {
                        existingViajante.setFotoContentType(viajante.getFotoContentType());
                    }
                    if (viajante.getDataNascimento() != null) {
                        existingViajante.setDataNascimento(viajante.getDataNascimento());
                    }
                    if (viajante.getStatusv() != null) {
                        existingViajante.setStatusv(viajante.getStatusv());
                    }

                    return existingViajante;
                }
            )
            .map(viajanteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Viajante> findAll(Pageable pageable) {
        log.debug("Request to get all Viajantes");
        return viajanteRepository.findAll(pageable);
    }

    public Page<Viajante> findAllWithEagerRelationships(Pageable pageable) {
        return viajanteRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Viajante> findOne(Long id) {
        log.debug("Request to get Viajante : {}", id);
        return viajanteRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Viajante : {}", id);
        viajanteRepository.deleteById(id);
    }
}
