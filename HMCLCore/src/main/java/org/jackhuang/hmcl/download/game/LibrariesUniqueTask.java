/*
 * Hello Minecraft! Launcher.
 * Copyright (C) 2017  huangyuhui <huanghongxun2008@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */
package org.jackhuang.hmcl.download.game;

import org.jackhuang.hmcl.game.Library;
import org.jackhuang.hmcl.game.Version;
import org.jackhuang.hmcl.task.TaskResult;
import org.jackhuang.hmcl.util.SimpleMultimap;
import org.jackhuang.hmcl.util.VersionNumber;

import java.util.*;
import java.util.stream.Collectors;

public class LibrariesUniqueTask extends TaskResult<Version> {
    private final Version version;
    private final String id;

    public LibrariesUniqueTask(Version version) {
        this(version, "version");
    }

    public LibrariesUniqueTask(Version version, String id) {
        this.version = version;
        this.id = id;
    }

    @Override
    public void execute() throws Exception {
        List<Library> libraries = new ArrayList<>(version.getLibraries());

        Map<String, VersionNumber> versionMap = new HashMap<>();
        SimpleMultimap<String, Library> multimap = new SimpleMultimap<String, Library>(HashMap::new, LinkedList::new);

        for (Library library : libraries) {
            String id = library.getGroupId() + ":" + library.getArtifactId();
            VersionNumber number = VersionNumber.asVersion(library.getVersion());

            if (versionMap.containsKey(id)) {
                VersionNumber otherNumber = versionMap.get(id);
                if (number.compareTo(otherNumber) > 0) {
                    multimap.removeKey(id);
                    versionMap.put(id, number);
                    multimap.put(id, library);
                } else if (number.compareTo(otherNumber) == 0) {
                    multimap.put(id, library);
                }
            } else {
                versionMap.put(id, number);
                multimap.put(id, library);
            }
        }

        setResult(version.setLibraries(multimap.values().stream().sorted().collect(Collectors.toList())));
    }

    @Override
    public String getId() {
        return id;
    }
}
