package com.rafaelfiume.db.plugin;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.text.NumberFormat;

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
        //return isGraterThan(from) && isLesserThanOrEqualTo(to);

        if (this.majorAsInt() < from.majorAsInt()) return false;
        if (this.majorAsInt() > to.majorAsInt())   return false;

        // Major are equals
        // i01.01 i01.02

        // Major is bigger
        // i02.01 i01.02
        if (this.majorAsInt() > from.majorAsInt() && this.majorAsInt() <= from.minorAsInt()) return true; // Duplicated

        return this.minorAsInt() >= from.minorAsInt() && this.minorAsInt() <= to.minorAsInt();
    }

    private boolean isGraterThan(Version from) {
        return this.majorAsInt() > from.majorAsInt()
                && this.minorAsInt() > from.minorAsInt();
    }

    private boolean isLesserThanOrEqualTo(Version to) {
        return this.minorAsInt() <= to.minorAsInt()
                && this.minorAsInt() <= to.minorAsInt();
    }

    private int majorAsInt() {
        return Integer.parseInt(major.replace("i", ""));
    }

    private int minorAsInt() {
        return Integer.parseInt(minor);
    }

}
