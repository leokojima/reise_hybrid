package com.reise.reise.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.reise.reise.IntegrationTest;
import com.reise.reise.domain.Local;
import com.reise.reise.domain.enumeration.Status;
import com.reise.reise.repository.LocalRepository;
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
 * Integration tests for the {@link LocalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LocalResourceIT {

    private static final String DEFAULT_NOMELOCAL = "AAAAAAAAAA";
    private static final String UPDATED_NOMELOCAL = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final byte[] DEFAULT_FOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOTO_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TIPO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUSL = Status.ATIVO;
    private static final Status UPDATED_STATUSL = Status.SUSPENSO;

    private static final String ENTITY_API_URL = "/api/locals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalMockMvc;

    private Local local;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createEntity(EntityManager em) {
        Local local = new Local()
            .nomelocal(DEFAULT_NOMELOCAL)
            .email(DEFAULT_EMAIL)
            .foto(DEFAULT_FOTO)
            .fotoContentType(DEFAULT_FOTO_CONTENT_TYPE)
            .tipo(DEFAULT_TIPO)
            .statusl(DEFAULT_STATUSL);
        return local;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Local createUpdatedEntity(EntityManager em) {
        Local local = new Local()
            .nomelocal(UPDATED_NOMELOCAL)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .tipo(UPDATED_TIPO)
            .statusl(UPDATED_STATUSL);
        return local;
    }

    @BeforeEach
    public void initTest() {
        local = createEntity(em);
    }

    @Test
    @Transactional
    void createLocal() throws Exception {
        int databaseSizeBeforeCreate = localRepository.findAll().size();
        // Create the Local
        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isCreated());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeCreate + 1);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNomelocal()).isEqualTo(DEFAULT_NOMELOCAL);
        assertThat(testLocal.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLocal.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testLocal.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testLocal.getTipo()).isEqualTo(DEFAULT_TIPO);
        assertThat(testLocal.getStatusl()).isEqualTo(DEFAULT_STATUSL);
    }

    @Test
    @Transactional
    void createLocalWithExistingId() throws Exception {
        // Create the Local with an existing ID
        local.setId(1L);

        int databaseSizeBeforeCreate = localRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomelocalIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setNomelocal(null);

        // Create the Local, which fails.

        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isBadRequest());

        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setEmail(null);

        // Create the Local, which fails.

        restLocalMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isBadRequest());

        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocals() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the localList
        restLocalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomelocal").value(hasItem(DEFAULT_NOMELOCAL)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].fotoContentType").value(hasItem(DEFAULT_FOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].foto").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOTO))))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO)))
            .andExpect(jsonPath("$.[*].statusl").value(hasItem(DEFAULT_STATUSL.toString())));
    }

    @Test
    @Transactional
    void getLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get the local
        restLocalMockMvc
            .perform(get(ENTITY_API_URL_ID, local.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(local.getId().intValue()))
            .andExpect(jsonPath("$.nomelocal").value(DEFAULT_NOMELOCAL))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.fotoContentType").value(DEFAULT_FOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.foto").value(Base64Utils.encodeToString(DEFAULT_FOTO)))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO))
            .andExpect(jsonPath("$.statusl").value(DEFAULT_STATUSL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingLocal() throws Exception {
        // Get the local
        restLocalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local
        Local updatedLocal = localRepository.findById(local.getId()).get();
        // Disconnect from session so that the updates on updatedLocal are not directly saved in db
        em.detach(updatedLocal);
        updatedLocal
            .nomelocal(UPDATED_NOMELOCAL)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .tipo(UPDATED_TIPO)
            .statusl(UPDATED_STATUSL);

        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocal.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNomelocal()).isEqualTo(UPDATED_NOMELOCAL);
        assertThat(testLocal.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLocal.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testLocal.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testLocal.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testLocal.getStatusl()).isEqualTo(UPDATED_STATUSL);
    }

    @Test
    @Transactional
    void putNonExistingLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, local.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalWithPatch() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local using partial update
        Local partialUpdatedLocal = new Local();
        partialUpdatedLocal.setId(local.getId());

        partialUpdatedLocal.nomelocal(UPDATED_NOMELOCAL).tipo(UPDATED_TIPO);

        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNomelocal()).isEqualTo(UPDATED_NOMELOCAL);
        assertThat(testLocal.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testLocal.getFoto()).isEqualTo(DEFAULT_FOTO);
        assertThat(testLocal.getFotoContentType()).isEqualTo(DEFAULT_FOTO_CONTENT_TYPE);
        assertThat(testLocal.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testLocal.getStatusl()).isEqualTo(DEFAULT_STATUSL);
    }

    @Test
    @Transactional
    void fullUpdateLocalWithPatch() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local using partial update
        Local partialUpdatedLocal = new Local();
        partialUpdatedLocal.setId(local.getId());

        partialUpdatedLocal
            .nomelocal(UPDATED_NOMELOCAL)
            .email(UPDATED_EMAIL)
            .foto(UPDATED_FOTO)
            .fotoContentType(UPDATED_FOTO_CONTENT_TYPE)
            .tipo(UPDATED_TIPO)
            .statusl(UPDATED_STATUSL);

        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocal.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLocal))
            )
            .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = localList.get(localList.size() - 1);
        assertThat(testLocal.getNomelocal()).isEqualTo(UPDATED_NOMELOCAL);
        assertThat(testLocal.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testLocal.getFoto()).isEqualTo(UPDATED_FOTO);
        assertThat(testLocal.getFotoContentType()).isEqualTo(UPDATED_FOTO_CONTENT_TYPE);
        assertThat(testLocal.getTipo()).isEqualTo(UPDATED_TIPO);
        assertThat(testLocal.getStatusl()).isEqualTo(UPDATED_STATUSL);
    }

    @Test
    @Transactional
    void patchNonExistingLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, local.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(local))
            )
            .andExpect(status().isBadRequest());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocal() throws Exception {
        int databaseSizeBeforeUpdate = localRepository.findAll().size();
        local.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(local)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Local in the database
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        int databaseSizeBeforeDelete = localRepository.findAll().size();

        // Delete the local
        restLocalMockMvc
            .perform(delete(ENTITY_API_URL_ID, local.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Local> localList = localRepository.findAll();
        assertThat(localList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
