package com.reise.reise.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.reise.reise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompartilhamentoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Compartilhamento.class);
        Compartilhamento compartilhamento1 = new Compartilhamento();
        compartilhamento1.setId(1L);
        Compartilhamento compartilhamento2 = new Compartilhamento();
        compartilhamento2.setId(compartilhamento1.getId());
        assertThat(compartilhamento1).isEqualTo(compartilhamento2);
        compartilhamento2.setId(2L);
        assertThat(compartilhamento1).isNotEqualTo(compartilhamento2);
        compartilhamento1.setId(null);
        assertThat(compartilhamento1).isNotEqualTo(compartilhamento2);
    }
}
