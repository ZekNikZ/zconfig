package io.zkz.zconfig.config;

import io.zkz.zconfig.spec.ConfigSpec;

// TODO: config class, base class - get / set methods
// TODO: MemoryConfig, FileConfig, WatchedFileConfig
// TODO: subclass is BoundConfig that has a .data object attache
public interface Config {
    /// Get spec of this config
    ConfigSpec getSpec();

    /// (Re)load data
    void load();

    /// Save data
    void save(boolean block);

    /// Save data
    default void save() {
        save(false);
    }

    /// Wait for outstanding save operations to finish
    void close();

    /// Check if the current config values adhere to a different spec
    boolean adheresTo(ConfigSpec otherSpec);

    /// Add a listener for changes to config
    void addListener(ConfigListener listener);

    /// Remove a change listener
    void removeListener(ConfigListener listener);

    @FunctionalInterface
    interface ConfigListener {
        void onConfigChange(Config config);
    }
}
