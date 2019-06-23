package net.adamcin.oakpal.core;

import static net.adamcin.oakpal.core.JavaxJson.hasNonNull;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.apache.jackrabbit.spi.QNodeTypeDefinition;
import org.apache.jackrabbit.spi.commons.namespace.NamespaceMapping;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A chart is a reproducible execution plan, similar in design to a Checklist, but with the following differences:
 * 1. Identified by URL, supporting retrieval on the classpath.
 * 2. No support for referencing external CND files.
 * 3. Can reference pre-install packages by URL.
 * 4. Can not aggregate multiple charts per execution.
 */
public final class Chart implements JavaxJson.ObjectConvertible {
    public static final URL DEFAULT_CHART_URL = Chart.class.getResource("default-chart.json");
    public static final String KEY_CHECKLISTS = "checklists";
    public static final String KEY_CHECKS = "checks";
    public static final String KEY_FORCED_ROOTS = "forcedRoots";
    public static final String KEY_JCR_NAMESPACES = "jcrNamespaces";
    public static final String KEY_JCR_NODETYPES = "jcrNodetypes";
    public static final String KEY_JCR_PRIVILEGES = "jcrPrivileges";
    public static final String KEY_PREINSTALL_URLS = "preInstallUrls";

    private final URL base;
    private final JsonObject originalJson;
    private final List<String> checklists;
    private final List<URL> preInstallUrls;
    private final List<JcrNs> jcrNamespaces;
    private final List<QNodeTypeDefinition> jcrNodetypes;
    private final List<String> jcrPrivileges;
    private final List<ForcedRoot> forcedRoots;
    private final List<CheckSpec> checks;

    private Chart(final @Nullable URL base,
                  final @Nullable JsonObject originalJson,
                  final @NotNull List<String> checklists,
                  final @NotNull List<URL> preInstallUrls,
                  final @NotNull List<JcrNs> jcrNamespaces,
                  final @NotNull List<QNodeTypeDefinition> jcrNodetypes,
                  final @NotNull List<String> jcrPrivileges,
                  final @NotNull List<ForcedRoot> forcedRoots,
                  final @NotNull List<CheckSpec> checks) {
        this.base = base;
        this.originalJson = originalJson;
        this.checklists = checklists;
        this.preInstallUrls = preInstallUrls;
        this.jcrNamespaces = jcrNamespaces;
        this.jcrNodetypes = jcrNodetypes;
        this.jcrPrivileges = jcrPrivileges;
        this.forcedRoots = forcedRoots;
        this.checks = checks;
    }

    public JsonObject getOriginalJson() {
        return originalJson;
    }

    public List<String> getChecklists() {
        return checklists;
    }

    public List<URL> getPreInstallUrls() {
        return preInstallUrls;
    }

    public List<JcrNs> getJcrNamespaces() {
        return jcrNamespaces;
    }

    public List<QNodeTypeDefinition> getJcrNodetypes() {
        return jcrNodetypes;
    }

    public List<String> getJcrPrivileges() {
        return jcrPrivileges;
    }

    public List<ForcedRoot> getForcedRoots() {
        return forcedRoots;
    }

    public List<CheckSpec> getChecks() {
        return checks;
    }

    @Override
    public JsonObject toJson() {
        if (this.originalJson != null) {
            return this.originalJson;
        }

        return JavaxJson.obj()
                .key(KEY_PREINSTALL_URLS).opt(preInstallUrls)
                .key(KEY_CHECKLISTS).opt(checklists)
                .key(KEY_CHECKS).opt(checks)
                .key(KEY_FORCED_ROOTS).opt(forcedRoots)
                .key(KEY_JCR_NODETYPES).opt(jcrNodetypes)
                .key(KEY_JCR_PRIVILEGES).opt(jcrPrivileges)
                .key(KEY_JCR_NAMESPACES).opt(jcrNamespaces)
                .get();
    }

    InitStage toInitStage() {
        InitStage.Builder builder = new InitStage.Builder();

        for (JcrNs ns : jcrNamespaces) {
            builder = builder.withNs(ns.getPrefix(), ns.getUri());
        }

        builder.withQNodeTypes(jcrNodetypes);

        for (String privilege : jcrPrivileges) {
            builder = builder.withPrivilege(privilege);
        }

        for (ForcedRoot forcedRoot : forcedRoots) {
            builder = builder.withForcedRoot(forcedRoot);
        }

        return builder.build();
    }

    public OakMachine.Builder toOakMachineBuilder(final ErrorListener errorListener, final ClassLoader classLoader) throws Exception {
        final ChecklistPlanner checklistPlanner = new ChecklistPlanner(checklists);
        checklistPlanner.discoverChecklists(classLoader);

        final List<ProgressCheck> allChecks;
        try {
            allChecks = new ArrayList<>(Locator.loadFromCheckSpecs(
                    checklistPlanner.getEffectiveCheckSpecs(checks), classLoader));
        } catch (final Exception e) {
            throw new Exception("Error while loading progress checks.", e);
        }

        return new OakMachine.Builder()
                .withErrorListener(errorListener)
                .withProgressChecks(allChecks)
                .withInitStages(checklistPlanner.getInitStages())
                .withInitStage(toInitStage())
                .withPreInstallUrls(preInstallUrls);
    }

    public static Result<Chart> fromJson(final @NotNull URL jsonUrl) {
        try (JsonReader reader = Json.createReader(jsonUrl.openStream())) {
            JsonObject json = reader.readObject();
            Chart.Builder builder = new Chart.Builder(jsonUrl);
            if (hasNonNull(json, KEY_PREINSTALL_URLS)) {
                builder.withPreInstallUrls(JavaxJson.mapArrayOfStrings(json.getJsonArray(KEY_PREINSTALL_URLS),
                        Fun.uncheck1(url -> new URL(jsonUrl, url))));
            }
            if (hasNonNull(json, KEY_CHECKLISTS)) {
                builder.withChecklists(JavaxJson.mapArrayOfStrings(json.getJsonArray(KEY_CHECKLISTS)));
            }
            if (hasNonNull(json, KEY_CHECKS)) {
                builder.withChecks(JavaxJson.mapArrayOfObjects(json.getJsonArray(KEY_CHECKS), CheckSpec::fromJson));
            }
            if (hasNonNull(json, KEY_FORCED_ROOTS)) {
                builder.withForcedRoots(JavaxJson.mapArrayOfObjects(json.getJsonArray(KEY_FORCED_ROOTS), ForcedRoot::fromJson));
            }
            final NamespaceMapping mapping;
            if (hasNonNull(json, KEY_JCR_NAMESPACES)) {
                final List<JcrNs> jcrNsList = JavaxJson.mapArrayOfObjects(json.getJsonArray(KEY_JCR_NAMESPACES),
                        JcrNs::fromJson);
                mapping = JsonCnd.toNamespaceMapping(jcrNsList);
                builder.withJcrNamespaces(jcrNsList);
            } else {
                mapping = QName.BUILTIN_MAPPINGS;
            }
            if (hasNonNull(json, KEY_JCR_NODETYPES)) {
                builder.withJcrNodetypes(JsonCnd.getQTypesFromJson(json.getJsonObject(KEY_JCR_NODETYPES), mapping));
            }
            if (hasNonNull(json, KEY_JCR_PRIVILEGES)) {
                builder.withJcrPrivileges(JavaxJson.mapArrayOfStrings(json.getJsonArray(KEY_JCR_PRIVILEGES)));
            }
            return Result.success(builder.build(json));
        } catch (IOException e) {
            return Result.failure(e);
        }
    }

    public static final class Builder {
        private final URL base;
        private List<String> checklists = Collections.emptyList();
        private List<URL> preInstallUrls = Collections.emptyList();
        private List<JcrNs> jcrNamespaces = Collections.emptyList();
        private List<QNodeTypeDefinition> jcrNodetypes = Collections.emptyList();
        private List<String> jcrPrivileges = Collections.emptyList();
        private List<ForcedRoot> forcedRoots = Collections.emptyList();
        private List<CheckSpec> checks = Collections.emptyList();

        public Builder(final URL base) {
            this.base = base;
        }

        public Builder withChecklists(List<String> checklists) {
            this.checklists = new ArrayList<>(checklists);
            return this;
        }

        public Builder withPreInstallUrls(List<URL> preInstallUrls) {
            this.preInstallUrls = new ArrayList<>(preInstallUrls);
            return this;
        }

        public Builder withJcrNamespaces(List<JcrNs> jcrNamespaces) {
            this.jcrNamespaces = new ArrayList<>(jcrNamespaces);
            return this;
        }

        public Builder withJcrNodetypes(List<QNodeTypeDefinition> jcrNodetypes) {
            this.jcrNodetypes = new ArrayList<>(jcrNodetypes);
            return this;
        }

        public Builder withJcrPrivileges(List<String> jcrPrivileges) {
            this.jcrPrivileges = new ArrayList<>(jcrPrivileges);
            return this;
        }

        public Builder withForcedRoots(List<ForcedRoot> forcedRoots) {
            this.forcedRoots = new ArrayList<>(forcedRoots);
            return this;
        }

        public Builder withChecks(List<CheckSpec> checks) {
            this.checks = new ArrayList<>(checks);
            return this;
        }

        private Chart build(final @Nullable JsonObject originalJson) {
            return new Chart(base, originalJson, checklists, preInstallUrls, jcrNamespaces,
                    jcrNodetypes, jcrPrivileges, forcedRoots, checks);
        }

        public Chart build() {
            return build(null);
        }
    }
}