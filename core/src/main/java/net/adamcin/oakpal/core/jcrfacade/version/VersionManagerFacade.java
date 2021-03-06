/*
 * Copyright 2018 Mark Adamcin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.adamcin.oakpal.core.jcrfacade.version;

import net.adamcin.oakpal.core.ListenerReadOnlyException;
import net.adamcin.oakpal.core.jcrfacade.NodeFacade;
import net.adamcin.oakpal.core.jcrfacade.SessionFacade;
import org.jetbrains.annotations.NotNull;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.version.Version;
import javax.jcr.version.VersionHistory;
import javax.jcr.version.VersionManager;

/**
 * Wraps {@link VersionManager} to prevent version history modification and to wrap retrieved {@link Version} and
 * {@link VersionHistory} instances.
 */
public final class VersionManagerFacade<S extends Session> implements VersionManager {
    private final @NotNull VersionManager delegate;
    private final @NotNull SessionFacade<S> session;

    public VersionManagerFacade(final @NotNull VersionManager delegate, final @NotNull SessionFacade<S> session) {
        this.delegate = delegate;
        this.session = session;
    }

    @Override
    public boolean isCheckedOut(String absPath) throws RepositoryException {
        return delegate.isCheckedOut(absPath);
    }

    @Override
    public VersionHistory getVersionHistory(String absPath) throws RepositoryException {
        VersionHistory internal = delegate.getVersionHistory(absPath);
        return new VersionHistoryFacade<>(internal, session);
    }

    @Override
    public Version getBaseVersion(String absPath) throws RepositoryException {
        Version internal = delegate.getBaseVersion(absPath);
        return new VersionFacade<>(internal, session);
    }

    @Override
    public Node getActivity() throws RepositoryException {
        return NodeFacade.wrap(delegate.getActivity(), session);
    }

    @Override
    public Version checkin(String absPath) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void checkout(String absPath) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public Version checkpoint(String absPath) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void restore(Version[] versions, boolean removeExisting) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void restore(String absPath, String versionName, boolean removeExisting) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void restore(Version version, boolean removeExisting) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void restore(String absPath, Version version, boolean removeExisting) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void restoreByLabel(String absPath, String versionLabel, boolean removeExisting) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public NodeIterator merge(String absPath, String srcWorkspace, boolean bestEffort) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public NodeIterator merge(String absPath, String srcWorkspace, boolean bestEffort, boolean isShallow) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public NodeIterator merge(Node activityNode) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void doneMerge(String absPath, Version version) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void cancelMerge(String absPath, Version version) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public Node createConfiguration(String absPath) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public Node setActivity(Node activity) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public Node createActivity(String title) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

    @Override
    public void removeActivity(Node activityNode) throws RepositoryException {
        throw new ListenerReadOnlyException();
    }

}
