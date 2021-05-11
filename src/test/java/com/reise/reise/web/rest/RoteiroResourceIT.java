package com.reise.reise.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reise.reise.IntegrationTest;
import com.reise.reise.domain.Roteiro;
import com.reise.reise.domain.enumeration.Status;
import com.reise.reise.repository.RoteiroRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoteiroResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoteiroResourceIT {

    private static final String DEFAULT_NOMEROTEIRO = "AAAAAAAAAA";
    private static final String UPDATED_NOMEROTEIRO = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUSR = Status.ATIVO;
    private static final Status UPDATED_STATUSR = Status.SUSPENSO;

    private static final String ENTITY_API_URL = "/api/roteiros";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoteiroRepository roteiroRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoteiroMockMvc;

    private Roteiro roteiro;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roteiro createEntity(EntityManager em) {
        Roteiro roteiro = new Roteiro().nomeroteiro(DEFAULT_NOMEROTEIRO).tipo(DEFAULT_TIPO).statusr(DEFAULT_STATUSR);
        return roteiro;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Roteiro createUpdatedEntity(EntityManager em) {
        Roteiro roteiro = new Roteiro().nomeroteiro(UPDATED_NOMEROTEIRO).tipo(UPDATED_TIPO).statusr(UPDATED_STATUSR);
        return roteiro;
    }

    @BeforeEach
    public void initTest() {
        roteiro = createEntity(em);
    }

    @Test
    @Transactional
    void createRoteiro() throws Exception {
        int databaseSizeBeforeCreate = roteiroRepository.findAll().size();
        // Create the Roteiro
        restRoteiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteiro)))
            .andExpect(status().isCreated());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeCreate + 1);
        Roteiro testRoteiro = roteiroList.get(roteiroList.size() - 1);
        assertThat(testRoteiro.getNomeroteiro()).isEqualTo(DEFAULT_NOMEROTEIRO);
        assertThat(testRoteiro.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testRoteiro.getStatusr()).isEqualTo(DEFAULT_STATUSR);
    }

    @Test
    @Transactional
    void createRoteiroWithExistingId() throws Exception {
        // Create the Roteiro with an existing ID
        roteiro.setId(1L);

        int databaseSizeBeforeCreate = roteiroRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoteiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteiro)))
            .andExpect(status().isBadRequest());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeroteiroIsRequired() throws Exception {
        int databaseSizeBeforeTest = roteiroRepository.findAll().size();
        // set the field null
        roteiro.setNomeroteiro(null);

        // Create the Roteiro, which fails.

        restRoteiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteiro)))
            .andExpect(status().isBadRequest());

        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        int databaseSizeBeforeTest = roteiroRepository.findAll().size();
        // set the field null
        roteiro.setTipo(null);

        // Create the Roteiro, which fails.

        restRoteiroMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteiro)))
            .andExpect(status().isBadRequest());

        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoteiros() throws Exception {
        // Initialize the database
        roteiroRepository.saveAndFlush(roteiro);

        // Get all the roteiroList
        restRoteiroMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roteiro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeroteiro").value(hasItem(DEFAULT_NOMEROTEIRO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].statusr").value(hasItem(DEFAULT_STATUSR.toString())));
    }

    @Test
    @Transactional
    void getRoteiro() throws Exception {
        // Initialize the database
        roteiroRepository.saveAndFlush(roteiro);

        // Get the roteiro
        restRoteiroMockMvc
            .perform(get(ENTITY_API_URL_ID, roteiro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roteiro.getId().intValue()))
            .andExpect(jsonPath("$.nomeroteiro").value(DEFAULT_NOMEROTEIRO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.statusr").value(DEFAULT_STATUSR.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRoteiro() throws Exception {
        // Get the roteiro
        restRoteiroMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoteiro() throws Exception {
        // Initialize the database
        roteiroRepository.saveAndFlush(roteiro);

        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();

        // Update the roteiro
        Roteiro updatedRoteiro = roteiroRepository.findById(roteiro.getId()).get();
        // Disconnect from session so that the updates on updatedRoteiro are not directly saved in db
        em.detach(updatedRoteiro);
        updatedRoteiro.nomeroteiro(UPDATED_NOMEROTEIRO).tipo(UPDATED_TIPO).statusr(UPDATED_STATUSR);

        restRoteiroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoteiro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoteiro))
            )
            .andExpect(status().isOk());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
        Roteiro testRoteiro = roteiroList.get(roteiroList.size() - 1);
        assertThat(testRoteiro.getNomeroteiro()).isEqualTo(UPDATED_NOMEROTEIRO);
        assertThat(testRoteiro.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testRoteiro.getStatusr()).isEqualTo(UPDATED_STATUSR);
    }

    @Test
    @Transactional
    void putNonExistingRoteiro() throws Exception {
        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();
        roteiro.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoteiroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roteiro.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roteiro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoteiro() throws Exception {
        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();
        roteiro.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoteiroMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roteiro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoteiro() throws Exception {
        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();
        roteiro.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoteiroMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roteiro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoteiroWithPatch() throws Exception {
        // Initialize the database
        roteiroRepository.saveAndFlush(roteiro);

        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();

        // Update the roteiro using partial update
        Roteiro partialUpdatedRoteiro = new Roteiro();
        partialUpdatedRoteiro.setId(roteiro.getId());

        partialUpdatedRoteiro.nomeroteiro(UPDATED_NOMEROTEIRO).tipo(UPDATED_TIPO);

        restRoteiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoteiro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoteiro))
            )
            .andExpect(status().isOk());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
        Roteiro testRoteiro = roteiroList.get(roteiroList.size() - 1);
        assertThat(testRoteiro.getNomeroteiro()).isEqualTo(UPDATED_NOMEROTEIRO);
        assertThat(testRoteiro.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testRoteiro.getStatusr()).isEqualTo(DEFAULT_STATUSR);
    }

    @Test
    @Transactional
    void fullUpdateRoteiroWithPatch() throws Exception {
        // Initialize the database
        roteiroRepository.saveAndFlush(roteiro);

        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();

        // Update the roteiro using partial update
        Roteiro partialUpdatedRoteiro = new Roteiro();
        partialUpdatedRoteiro.setId(roteiro.getId());

        partialUpdatedRoteiro.nomeroteiro(UPDATED_NOMEROTEIRO).tipo(UPDATED_TIPO).statusr(UPDATED_STATUSR);

        restRoteiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoteiro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoteiro))
            )
            .andExpect(status().isOk());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
        Roteiro testRoteiro = roteiroList.get(roteiroList.size() - 1);
        assertThat(testRoteiro.getNomeroteiro()).isEqualTo(UPDATED_NOMEROTEIRO);
        assertThat(testRoteiro.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testRoteiro.getStatusr()).isEqualTo(UPDATED_STATUSR);
    }

    @Test
    @Transactional
    void patchNonExistingRoteiro() throws Exception {
        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();
        roteiro.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoteiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roteiro.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roteiro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoteiro() throws Exception {
        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();
        roteiro.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoteiroMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roteiro))
            )
            .andExpect(status().isBadRequest());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoteiro() throws Exception {
        int databaseSizeBeforeUpdate = roteiroRepository.findAll().size();
        roteiro.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoteiroMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roteiro)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Roteiro in the database
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoteiro() throws Exception {
        // Initialize the database
        roteiroRepository.saveAndFlush(roteiro);

        int databaseSizeBeforeDelete = roteiroRepository.findAll().size();

        // Delete the roteiro
        restRoteiroMockMvc
            .perform(delete(ENTITY_API_URL_ID, roteiro.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Roteiro> roteiroList = roteiroRepository.findAll();
        assertThat(roteiroList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
