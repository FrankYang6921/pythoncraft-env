package top.frankyang.pre.misc;

import com.google.gson.JsonElement;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.*;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class FakeMetadata implements ModMetadata {
    private final String id;

    public FakeMetadata(String id) {
        this.id = id;
    }

    @Override
    public String getType() {
        return "fabric";
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Collection<String> getProvides() {
        return Collections.emptyList();
    }

    @Override
    public Version getVersion() {
        return null;
    }

    @Override
    public ModEnvironment getEnvironment() {
        return ModEnvironment.UNIVERSAL;
    }

    @Override
    public Collection<ModDependency> getDepends() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ModDependency> getRecommends() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ModDependency> getSuggests() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ModDependency> getConflicts() {
        return Collections.emptyList();
    }

    @Override
    public Collection<ModDependency> getBreaks() {
        return Collections.emptyList();
    }

    @Override
    public String getName() {
        return "<DUMMY>";
    }

    @Override
    public String getDescription() {
        return "<DUMMY>";
    }

    @Override
    public Collection<Person> getAuthors() {
        return Collections.emptyList();
    }

    @Override
    public Collection<Person> getContributors() {
        return Collections.emptyList();
    }

    @Override
    public ContactInformation getContact() {
        return ContactInformation.EMPTY;
    }

    @Override
    public Collection<String> getLicense() {
        return Collections.emptyList();
    }

    @Override
    public Optional<String> getIconPath(int size) {
        return Optional.empty();
    }

    @Override
    public boolean containsCustomValue(String key) {
        return false;
    }

    @Override
    public CustomValue getCustomValue(String key) {
        return null;
    }

    @Override
    public Map<String, CustomValue> getCustomValues() {
        return null;
    }

    @Override
    public boolean containsCustomElement(String key) {
        return false;
    }

    @Override
    public JsonElement getCustomElement(String key) {
        return null;
    }
}
