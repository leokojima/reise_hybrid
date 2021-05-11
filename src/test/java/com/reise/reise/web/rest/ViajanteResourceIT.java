package com.reise.reise.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reise.reise.IntegrationTest;
import com.reise.reise.domain.Viajante;
import com.reise.reise.domain.enumeration.Status;
import com.reise.reise.repository.ViajanteRepository;
import com.reise.reise.service.ViajanteService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link ViajanteResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ViajanteResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Status DEFAULT_STATUSV = Status.ATIVO;
    private static final Status UPDATED_STATUSV = Status.SUSPENSO;

    private static final String ENTITY_API_URL = "/api/viajantes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ViajanteRepository viajanteRepository;

    @Mock
    private ViajanteRepository viajanteRepositoryMock;

    @Mock
    private ViajanteService viajanteServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restViajanteMockMvc;

    private Viajante viajante;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Viajante createEntity(EntityManager em) {
        Viajante viajante = new Viajante()
            .nome(DEFAULT_NOME)
            .email(DEFAULT_EMAIL)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .statusv(DEFAULT_STATUSV);
        return viajante;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Viajante createUpdatedEntity(EntityManager em) {
        Viajante viajante = new Viajante()
            .nome(UPDATED_NOME)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .statusv(UPDATED_STATUSV);
        return viajante;
    }

    @BeforeEach
    public void initTest() {
        viajante = createEntity(em);
    }

    @Test
    @Transactional
    void createViajante() throws Exception {
        int databaseSizeBeforeCreate = viajanteRepository.findAll().size();
        // Create the Viajante
        restViajanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viajante)))
            .andExpect(status().isCreated());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeCreate + 1);
        Viajante testViajante = viajanteList.get(viajanteList.size() - 1);
        assertThat(testViajante.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testViajante.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testViajante.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testViajante.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testViajante.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testViajante.getStatusv()).isEqualTo(DEFAULT_STATUSV);
    }

    @Test
    @Transactional
    void createViajanteWithExistingId() throws Exception {
        // Create the Viajante with an existing ID
        viajante.setId(1L);

        int databaseSizeBeforeCreate = viajanteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViajanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viajante)))
            .andExpect(status().isBadRequest());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = viajanteRepository.findAll().size();
        // set the field null
        viajante.setNome(null);

        // Create the Viajante, which fails.

        restViajanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viajante)))
            .andExpect(status().isBadRequest());

        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = viajanteRepository.findAll().size();
        // set the field null
        viajante.setEmail(null);

        // Create the Viajante, which fails.

        restViajanteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viajante)))
            .andExpect(status().isBadRequest());

        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllViajantes() throws Exception {
        // Initialize the database
        viajanteRepository.saveAndFlush(viajante);

        // Get all the viajanteList
        restViajanteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viajante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].statusv").value(hasItem(DEFAULT_STATUSV.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllViajantesWithEagerRelationshipsIsEnabled() throws Exception {
        when(viajanteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restViajanteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(viajanteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllViajantesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(viajanteServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restViajanteMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(viajanteServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getViajante() throws Exception {
        // Initialize the database
        viajanteRepository.saveAndFlush(viajante);

        // Get the viajante
        restViajanteMockMvc
            .perform(get(ENTITY_API_URL_ID, viajante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(viajante.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.statusv").value(DEFAULT_STATUSV.toString()));
    }

    @Test
    @Transactional
    void getNonExistingViajante() throws Exception {
        // Get the viajante
        restViajanteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewViajante() throws Exception {
        // Initialize the database
        viajanteRepository.saveAndFlush(viajante);

        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();

        // Update the viajante
        Viajante updatedViajante = viajanteRepository.findById(viajante.getId()).get();
        // Disconnect from session so that the updates on updatedViajante are not directly saved in db
        em.detach(updatedViajante);
        updatedViajante
            .nome(UPDATED_NOME)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .statusv(UPDATED_STATUSV);

        restViajanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedViajante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedViajante))
            )
            .andExpect(status().isOk());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
        Viajante testViajante = viajanteList.get(viajanteList.size() - 1);
        assertThat(testViajante.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testViajante.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testViajante.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testViajante.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testViajante.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testViajante.getStatusv()).isEqualTo(UPDATED_STATUSV);
    }

    @Test
    @Transactional
    void putNonExistingViajante() throws Exception {
        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();
        viajante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViajanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viajante.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viajante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchViajante() throws Exception {
        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();
        viajante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViajanteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(viajante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamViajante() throws Exception {
        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();
        viajante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViajanteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(viajante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViajanteWithPatch() throws Exception {
        // Initialize the database
        viajanteRepository.saveAndFlush(viajante);

        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();

        // Update the viajante using partial update
        Viajante partialUpdatedViajante = new Viajante();
        partialUpdatedViajante.setId(viajante.getId());

        partialUpdatedViajante.email(UPDATED_EMAIL).foto(UPDATED_FOTO).fotoContentType(UPDATED_FOTO_CONTENT_TYPE);

        restViajanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViajante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViajante))
            )
            .andExpect(status().isOk());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
        Viajante testViajante = viajanteList.get(viajanteList.size() - 1);
        assertThat(testViajante.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testViajante.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testViajante.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testViajante.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testViajante.getDataNascimento()).isEqualTo(DEFAULT_DATA_NASCIMENTO);
        assertThat(testViajante.getStatusv()).isEqualTo(DEFAULT_STATUSV);
    }

    @Test
    @Transactional
    void fullUpdateViajanteWithPatch() throws Exception {
        // Initialize the database
        viajanteRepository.saveAndFlush(viajante);

        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();

        // Update the viajante using partial update
        Viajante partialUpdatedViajante = new Viajante();
        partialUpdatedViajante.setId(viajante.getId());

        partialUpdatedViajante
            .nome(UPDATED_NOME)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .statusv(UPDATED_STATUSV);

        restViajanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViajante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedViajante))
            )
            .andExpect(status().isOk());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
        Viajante testViajante = viajanteList.get(viajanteList.size() - 1);
        assertThat(testViajante.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testViajante.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testViajante.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testViajante.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testViajante.getDataNascimento()).isEqualTo(UPDATED_DATA_NASCIMENTO);
        assertThat(testViajante.getStatusv()).isEqualTo(UPDATED_STATUSV);
    }

    @Test
    @Transactional
    void patchNonExistingViajante() throws Exception {
        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();
        viajante.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViajanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viajante.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viajante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchViajante() throws Exception {
        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();
        viajante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViajanteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(viajante))
            )
            .andExpect(status().isBadRequest());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamViajante() throws Exception {
        int databaseSizeBeforeUpdate = viajanteRepository.findAll().size();
        viajante.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViajanteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(viajante)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Viajante in the database
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteViajante() throws Exception {
        // Initialize the database
        viajanteRepository.saveAndFlush(viajante);

        int databaseSizeBeforeDelete = viajanteRepository.findAll().size();

        // Delete the viajante
        restViajanteMockMvc
            .perform(delete(ENTITY_API_URL_ID, viajante.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Viajante> viajanteList = viajanteRepository.findAll();
        assertThat(viajanteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
