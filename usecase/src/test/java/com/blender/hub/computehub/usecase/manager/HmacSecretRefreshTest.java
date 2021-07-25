package com.blender.hub.computehub.usecase.manager;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.usecase.CreateHmacSecretImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class HmacSecretRefreshTest extends AbstractManagerLinkingTest {

    public static final String NEW_SECRET_ID = "new secret id";
    public static final String NEW_SECRET_VALUE = "new secret value";

    @BeforeEach
    void setupUsecase() {
        createHmacSecret = new CreateHmacSecretImpl(hmacIdGenerator, hmacSecretRepository, hmacSecretValueGenerator);
    }

    @Test
    void secretRefreshGeneratesNewSecretId() {
        HmacSecret oldSecret = newStoredHmacSecret();
        when(hmacIdGenerator.generate()).thenReturn(NEW_SECRET_ID);

        HmacSecret newSecret = createHmacSecret.refresh(oldSecret);

        verify(hmacIdGenerator, times(1)).generate();
        Assertions.assertThat(newSecret.getId()).isEqualTo(NEW_SECRET_ID);
    }
    
    @Test
    void secretRefreshGeneratesNewSecretValue() {
        HmacSecret oldSecret = newStoredHmacSecret();
        when(hmacSecretValueGenerator.generate()).thenReturn(NEW_SECRET_VALUE);

        HmacSecret newSecret = createHmacSecret.refresh(oldSecret);

        verify(hmacSecretValueGenerator, times(1)).generate();
        Assertions.assertThat(newSecret.getValue()).isEqualTo(NEW_SECRET_VALUE);
    }

    @Test
    void secretRefreshDeletesOldSecret() {
        HmacSecret oldSecret = newStoredHmacSecret();

        createHmacSecret.refresh(oldSecret);

        verify(hmacSecretRepository, times(1)).deleteSecret(oldSecret.getId());
    }

    @Test
    void newSecretIsStored() {
        HmacSecret oldSecret = newStoredHmacSecret();

        HmacSecret newSecret = createHmacSecret.refresh(oldSecret);

        verify(hmacSecretRepository, times(1)).storeHmacSecret(newSecret);
    }

    @Test
    void exceptionOnOldSecretDeleteIsCaught() {
        HmacSecret oldSecret = newStoredHmacSecret();
        Mockito.doThrow(new RuntimeException("some exception"))
                .when(hmacSecretRepository).deleteSecret(Mockito.anyString());

        HmacSecret newSecret = createHmacSecret.refresh(oldSecret);
        verify(hmacSecretRepository, times(1)).storeHmacSecret(Mockito.eq(newSecret));
    }

    private HmacSecret newStoredHmacSecret() {
        return newStoredHmacSecret(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    private HmacSecret newStoredHmacSecret(String secretId, String secretValue) {
        return HmacSecret.builder()
                .id(secretId)
                .value(secretValue)
                .build();
    }
}
