package com.blender.hub.computehub.usecase.manager;

import com.blender.hub.computehub.entity.hmac.HmacSecret;
import com.blender.hub.computehub.usecase.hmac.port.driven.AuthenticationException;
import com.blender.hub.computehub.usecase.hmac.port.driven.HmacValidator;
import com.blender.hub.computehub.usecase.hmac.usecase.CreateHmacSecretImpl;
import com.blender.hub.computehub.usecase.hmac.usecase.HmacResetCommand;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class HmacSecretRefreshTest extends AbstractManagerLinkingTest {

    public static final String NEW_SECRET_ID = "9cbb53d7-7f23-4a53-8ff8-b976814fd0b5";
    public static final String NEW_SECRET_VALUE = "d091f27aaa7fb0561327b7f475e334dca112c1785d522310263c640d6042d5fa";

    public static final String PADDING = "a65553eea4dc3da49422610b40dba7963cd22d22d41c04d24aa7b0e34e493943";
    public static final String MANAGER_ID = "7c2ec618-abec-44d4-bb60-3db188b80902";
    public static final String OLD_SECRET_ID = "fde788f0-4c67-4f58-9438-b01387e99e88";
    public static final String OLD_SECRET_VALUE = "3979d3ccc29b5cb9e2417c236768e06476e3b9da689015805b3fec8032998a99";

    @Mock
    HmacValidator validator;

    @BeforeEach
    void setupUsecase() {
        createHmacSecret = new CreateHmacSecretImpl(hmacIdGenerator, hmacSecretRepository, hmacSecretValueGenerator,
                hmacSecret -> validator);
    }

    @Test
    void secretRefreshGeneratesNewSecretId() throws AuthenticationException {
        when(hmacIdGenerator.generate()).thenReturn(NEW_SECRET_ID);
        when(hmacSecretRepository.getForManager(Mockito.eq(MANAGER_ID)))
                .thenReturn(Optional.of(oldSecret()));

        HmacSecret newSecret = createHmacSecret.refresh(refreshCommand());

        verify(hmacIdGenerator, times(1)).generate();
        Assertions.assertThat(newSecret.getId()).isEqualTo(NEW_SECRET_ID);
    }


    @Test
    void secretRefreshGeneratesNewSecretValue() throws AuthenticationException {
        when(hmacSecretValueGenerator.generate()).thenReturn(NEW_SECRET_VALUE);
        when(hmacSecretRepository.getForManager(Mockito.eq(MANAGER_ID)))
                .thenReturn(Optional.of(oldSecret()));

        HmacSecret newSecret = createHmacSecret.refresh(refreshCommand());

        verify(hmacSecretValueGenerator, times(1)).generate();
        Assertions.assertThat(newSecret.getValue()).isEqualTo(NEW_SECRET_VALUE);
    }

    @Test
    void secretRefreshDeletesOldSecret() throws AuthenticationException {
        HmacSecret oldSecret = hmacSecret();
        when(hmacSecretRepository.getForManager(Mockito.eq(MANAGER_ID)))
                .thenReturn(Optional.of(oldSecret));

        createHmacSecret.refresh(refreshCommand());

        verify(hmacSecretRepository, times(1)).deleteSecret(oldSecret.getId());
    }

    @Test
    void newSecretIsStored() throws AuthenticationException {
        when(hmacSecretValueGenerator.generate()).thenReturn(NEW_SECRET_VALUE);
        when(hmacSecretRepository.getForManager(Mockito.eq(MANAGER_ID)))
                .thenReturn(Optional.of(oldSecret()));

        HmacSecret newSecret = createHmacSecret.refresh(refreshCommand());

        verify(hmacSecretRepository, times(1)).storeHmacSecret(newSecret);
    }

    @Test
    void exceptionOnOldSecretDeleteIsCaught() throws AuthenticationException {
        HmacSecret oldSecret = oldSecret();
        Mockito.doThrow(new RuntimeException("some exception"))
                .when(hmacSecretRepository).deleteSecret(Mockito.anyString());
        when(hmacSecretRepository.getForManager(Mockito.eq(MANAGER_ID)))
                .thenReturn(Optional.of(oldSecret));

        HmacSecret newSecret = createHmacSecret.refresh(refreshCommand());
        verify(hmacSecretRepository, times(1)).storeHmacSecret(Mockito.eq(newSecret));
    }

    private HmacResetCommand refreshCommand() {
        return HmacResetCommand.builder()
                .managerId(MANAGER_ID)
                .hmac(OLD_SECRET_ID)
                .padding(PADDING)
                .build();
    }

    // TODO: test hmac validation for refresh command throws exception

    private HmacSecret oldSecret() {
        return hmacSecret(OLD_SECRET_ID, OLD_SECRET_VALUE);
    }

    private HmacSecret hmacSecret() {
        return hmacSecret(UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    private HmacSecret hmacSecret(String secretId, String secretValue) {
        return HmacSecret.builder()
                .id(secretId)
                .value(secretValue)
                .build();
    }
}
