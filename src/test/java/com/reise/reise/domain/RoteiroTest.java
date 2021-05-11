package com.reise.reise.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reise.reise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoteiroTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Roteiro.class);
        Roteiro roteiro1 = new Roteiro();
        roteiro1.setId(1L);
        Roteiro roteiro2 = new Roteiro();
        roteiro2.setId(roteiro1.getId());
        assertThat(roteiro1).isEqualTo(roteiro2);
        roteiro2.setId(2L);
        assertThat(roteiro1).isNotEqualTo(roteiro2);
        roteiro1.setId(null);
        assertThat(roteiro1).isNotEqualTo(roteiro2);
    }
}
