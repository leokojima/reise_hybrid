package com.reise.reise.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reise.reise.IntegrationTest;
import com.reise.reise.domain.Compartilhamento;
import com.reise.reise.repository.CompartilhamentoRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CompartilhamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompartilhamentoResourceIT {

    private static final String DEFAULT_NOME_COMP = "AAAAAAAAAA";
    private static final String UPDATED_NOME_COMP = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO_COMP = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO_COMP = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_CRIACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_CRIACAO = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/compartilhamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompartilhamentoRepository compartilhamentoRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompartilhamentoMockMvc;

    private Compartilhamento compartilhamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compartilhamento createEntity(EntityManager em) {
        Compartilhamento compartilhamento = new Compartilhamento()
            .nomeComp(DEFAULT_NOME_COMP)
            .descricaoComp(DEFAULT_DESCRICAO_COMP)
            .dataCriacao(DEFAULT_DATA_CRIACAO);
        return compartilhamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compartilhamento createUpdatedEntity(EntityManager em) {
        Compartilhamento compartilhamento = new Compartilhamento()
            .nomeComp(UPDATED_NOME_COMP)
            .descricaoComp(UPDATED_DESCRICAO_COMP)
            .dataCriacao(UPDATED_DATA_CRIACAO);
        return compartilhamento;
    }

    @BeforeEach
    public void initTest() {
        compartilhamento = createEntity(em);
    }

    @Test
    @Transactional
    void createCompartilhamento() throws Exception {
        int databaseSizeBeforeCreate = compartilhamentoRepository.findAll().size();
        // Create the Compartilhamento
        restCompartilhamentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isCreated());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Compartilhamento testCompartilhamento = compartilhamentoList.get(compartilhamentoList.size() - 1);
        assertThat(testCompartilhamento.getNomeComp()).isEqualTo(DEFAULT_NOME_COMP);
        assertThat(testCompartilhamento.getDescricaoComp()).isEqualTo(DEFAULT_DESCRICAO_COMP);
        assertThat(testCompartilhamento.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void createCompartilhamentoWithExistingId() throws Exception {
        // Create the Compartilhamento with an existing ID
        compartilhamento.setId(1L);

        int databaseSizeBeforeCreate = compartilhamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompartilhamentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeCompIsRequired() throws Exception {
        int databaseSizeBeforeTest = compartilhamentoRepository.findAll().size();
        // set the field null
        compartilhamento.setNomeComp(null);

        // Create the Compartilhamento, which fails.

        restCompartilhamentoMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isBadRequest());

        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompartilhamentos() throws Exception {
        // Initialize the database
        compartilhamentoRepository.saveAndFlush(compartilhamento);

        // Get all the compartilhamentoList
        restCompartilhamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compartilhamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeComp").value(hasItem(DEFAULT_NOME_COMP)))
            .andExpect(jsonPath("$.[*].descricaoComp").value(hasItem(DEFAULT_DESCRICAO_COMP.toString())))
            .andExpect(jsonPath("$.[*].dataCriacao").value(hasItem(DEFAULT_DATA_CRIACAO.toString())));
    }

    @Test
    @Transactional
    void getCompartilhamento() throws Exception {
        // Initialize the database
        compartilhamentoRepository.saveAndFlush(compartilhamento);

        // Get the compartilhamento
        restCompartilhamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, compartilhamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compartilhamento.getId().intValue()))
            .andExpect(jsonPath("$.nomeComp").value(DEFAULT_NOME_COMP))
            .andExpect(jsonPath("$.descricaoComp").value(DEFAULT_DESCRICAO_COMP.toString()))
            .andExpect(jsonPath("$.dataCriacao").value(DEFAULT_DATA_CRIACAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCompartilhamento() throws Exception {
        // Get the compartilhamento
        restCompartilhamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompartilhamento() throws Exception {
        // Initialize the database
        compartilhamentoRepository.saveAndFlush(compartilhamento);

        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();

        // Update the compartilhamento
        Compartilhamento updatedCompartilhamento = compartilhamentoRepository.findById(compartilhamento.getId()).get();
        // Disconnect from session so that the updates on updatedCompartilhamento are not directly saved in db
        em.detach(updatedCompartilhamento);
        updatedCompartilhamento.nomeComp(UPDATED_NOME_COMP).descricaoComp(UPDATED_DESCRICAO_COMP).dataCriacao(UPDATED_DATA_CRIACAO);

        restCompartilhamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompartilhamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompartilhamento))
            )
            .andExpect(status().isOk());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
        Compartilhamento testCompartilhamento = compartilhamentoList.get(compartilhamentoList.size() - 1);
        assertThat(testCompartilhamento.getNomeComp()).isEqualTo(UPDATED_NOME_COMP);
        assertThat(testCompartilhamento.getDescricaoComp()).isEqualTo(UPDATED_DESCRICAO_COMP);
        assertThat(testCompartilhamento.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void putNonExistingCompartilhamento() throws Exception {
        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();
        compartilhamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompartilhamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compartilhamento.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompartilhamento() throws Exception {
        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();
        compartilhamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompartilhamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompartilhamento() throws Exception {
        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();
        compartilhamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompartilhamentoMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompartilhamentoWithPatch() throws Exception {
        // Initialize the database
        compartilhamentoRepository.saveAndFlush(compartilhamento);

        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();

        // Update the compartilhamento using partial update
        Compartilhamento partialUpdatedCompartilhamento = new Compartilhamento();
        partialUpdatedCompartilhamento.setId(compartilhamento.getId());

        partialUpdatedCompartilhamento.descricaoComp(UPDATED_DESCRICAO_COMP);

        restCompartilhamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompartilhamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompartilhamento))
            )
            .andExpect(status().isOk());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
        Compartilhamento testCompartilhamento = compartilhamentoList.get(compartilhamentoList.size() - 1);
        assertThat(testCompartilhamento.getNomeComp()).isEqualTo(DEFAULT_NOME_COMP);
        assertThat(testCompartilhamento.getDescricaoComp()).isEqualTo(UPDATED_DESCRICAO_COMP);
        assertThat(testCompartilhamento.getDataCriacao()).isEqualTo(DEFAULT_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void fullUpdateCompartilhamentoWithPatch() throws Exception {
        // Initialize the database
        compartilhamentoRepository.saveAndFlush(compartilhamento);

        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();

        // Update the compartilhamento using partial update
        Compartilhamento partialUpdatedCompartilhamento = new Compartilhamento();
        partialUpdatedCompartilhamento.setId(compartilhamento.getId());

        partialUpdatedCompartilhamento.nomeComp(UPDATED_NOME_COMP).descricaoComp(UPDATED_DESCRICAO_COMP).dataCriacao(UPDATED_DATA_CRIACAO);

        restCompartilhamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompartilhamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompartilhamento))
            )
            .andExpect(status().isOk());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
        Compartilhamento testCompartilhamento = compartilhamentoList.get(compartilhamentoList.size() - 1);
        assertThat(testCompartilhamento.getNomeComp()).isEqualTo(UPDATED_NOME_COMP);
        assertThat(testCompartilhamento.getDescricaoComp()).isEqualTo(UPDATED_DESCRICAO_COMP);
        assertThat(testCompartilhamento.getDataCriacao()).isEqualTo(UPDATED_DATA_CRIACAO);
    }

    @Test
    @Transactional
    void patchNonExistingCompartilhamento() throws Exception {
        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();
        compartilhamento.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompartilhamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compartilhamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompartilhamento() throws Exception {
        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();
        compartilhamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompartilhamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompartilhamento() throws Exception {
        int databaseSizeBeforeUpdate = compartilhamentoRepository.findAll().size();
        compartilhamento.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompartilhamentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compartilhamento))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compartilhamento in the database
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompartilhamento() throws Exception {
        // Initialize the database
        compartilhamentoRepository.saveAndFlush(compartilhamento);

        int databaseSizeBeforeDelete = compartilhamentoRepository.findAll().size();

        // Delete the compartilhamento
        restCompartilhamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, compartilhamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Compartilhamento> compartilhamentoList = compartilhamentoRepository.findAll();
        assertThat(compartilhamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
