package com.rafaelfiume.db.plugin;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

public final class Version {

    private final String major;
    private final String minor;

    public static Version newVersion(String major, String minor) {
        return new Version(major, minor);
    }

    private Version(String major, String minor) {
        this.major = major;
        this.minor = minor;
    }

    public String major() { return major; }
    public String minor() { return minor; }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, SHORT_PREFIX_STYLE);
    }

    public boolean isBetween(Version from, Version to) {
        return isGreaterThan(from) && isLesserThanOrEqualTo(to);
    }

    public boolean isGreaterThan(Version another) {
        if (this.majorAsInt() > another.majorAsInt()) return true;
        if (this.majorAsInt() == another.majorAsInt() && (this.minorAsInt() > another.minorAsInt())) return true;
        return false;
    }

    public boolean isLesserThanOrEqualTo(Version another) {
        return !isGreaterThan(another);
    }

    private int majorAsInt() {
        return Integer.parseInt(major.replace("i", ""));
    }

    private int minorAsInt() {
        return Integer.parseInt(minor);
    }
}
