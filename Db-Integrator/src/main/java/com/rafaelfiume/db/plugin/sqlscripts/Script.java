package com.rafaelfiume.db.plugin.sqlscripts;

import com.rafaelfiume.db.plugin.Version;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.nio.file.FileSystems;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rafaelfiume.db.plugin.Version.newVersion;
import static java.lang.String.format;

public final class Script {

    private final String name;
    private final Version version;

    private final Pattern versionPattern = Pattern.compile("^scripts/(i\\d+)/(\\d+).*$");

    public static Script newScript(String name) {
        return new Script(name);
    }

    public Script(String name) {
        this.name = name;
        this.version = extractVersionFrom(name);
    }

    public String name()     { return name; }
    public Version version() { return version; }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other, "versionPattern");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "versionPattern");
    }

    private Version extractVersionFrom(String name) {
        final String platformIndependentName = name.replace(FileSystems.getDefault().getSeparator(), "/");
        final Matcher m = versionPattern.matcher(platformIndependentName);
        if (!m.matches()) throw new IllegalArgumentException(format("could not retrieve version from: %s", name));

        return newVersion(m.group(1), m.group(2));
    }

    // TODO RF 25/04/2016 No sad path yet. #matches() ?

}
