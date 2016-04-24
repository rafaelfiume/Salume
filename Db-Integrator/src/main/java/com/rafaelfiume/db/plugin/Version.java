package com.rafaelfiume.db.plugin;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public final class Version {

    private final String major;
    private final String minor;

    public static Version newVersion(String majorVersion, String minorVersion) {
        return new Version(majorVersion, minorVersion);
    }

    private Version(String major, String minor) {
        this.major = major;
        this.minor = minor;
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
