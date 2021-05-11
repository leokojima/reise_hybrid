package com.reise.reise.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reise.reise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViajanteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Viajante.class);
        Viajante viajante1 = new Viajante();
        viajante1.setId(1L);
        Viajante viajante2 = new Viajante();
        viajante2.setId(viajante1.getId());
        assertThat(viajante1).isEqualTo(viajante2);
        viajante2.setId(2L);
        assertThat(viajante1).isNotEqualTo(viajante2);
        viajante1.setId(null);
        assertThat(viajante1).isNotEqualTo(viajante2);
    }
}
