package io.zkz.zconfig.spec.builders;

import io.zkz.zconfig.spec.properties.DateTimePropertySpec;

import java.time.Instant;

public class DateTimePropertySpecBuilder extends PropertySpecBuilder<String, Instant> {
    protected DateTimePropertySpecBuilder(String key) {
        super(key);
    }

    public DateTimePropertySpecBuilder after(Instant min) {
        this.validators.add(x -> !min.isAfter(x) ? null : "Instant '%s' is out of acceptable range: %s <= value".formatted(x, min));
        return this;
    }

    public DateTimePropertySpecBuilder before(Instant max) {
        this.validators.add(x -> !max.isBefore(x) ? null : "Instant '%s' is out of acceptable range: value <= %s".formatted(x, max));
        return this;
    }

    public DateTimePropertySpecBuilder range(Instant min, Instant max) {
        this.validators.add(x -> !min.isAfter(x) && !max.isBefore(x) ? null : "Instant '%s' is out of acceptable range: %s <= value <= %s".formatted(x, min, max));
        return this;
    }

    @Override
    public DateTimePropertySpec build() {
        return new DateTimePropertySpec(
            this.key,
            this.storageKey,
            this.comment,
            this.optional,
            this.defaultValueSupplier,
            this.validators
        );
    }
}
