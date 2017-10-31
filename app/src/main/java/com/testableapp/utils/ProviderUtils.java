package com.testableapp.utils;

import android.support.annotation.NonNull;

import com.testableapp.providers.AbstractProvider;
import com.testableapp.providers.FacebookProvider;

import java.util.HashMap;

public class ProviderUtils {

    private static final HashMap<String, AbstractProvider> providers = new HashMap<>();

    static {
        providers.put(AbstractProvider.PROVIDER_FACEBOOK, new FacebookProvider());
    }

    private ProviderUtils() {
        throw new AssertionError("Utility classes shouldn't be instantiated.");
    }

    /**
     * Gets provider by key
     *
     * @param providerKey the provider's key
     * @return the provider
     */
    @NonNull
    public static AbstractProvider getProvider(@NonNull final String providerKey) {
        if (!providerKey.contains(providerKey)) {
            throw new AssertionError("There's no configured provider for " + providerKey);
        }

        return providers.get(providerKey);
    }
}
