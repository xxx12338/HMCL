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
package org.jackhuang.hmcl.mod;

import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import org.jackhuang.hmcl.util.Immutable;
import org.jackhuang.hmcl.util.NetworkUtils;
import org.jackhuang.hmcl.util.Validation;

import java.net.URL;
import java.util.Objects;

/**
 *
 * @author huangyuhui
 */
@Immutable
public final class CurseManifestFile implements Validation {

    @SerializedName("projectID")
    private final int projectID;
    
    @SerializedName("fileID")
    private final int fileID;
    
    @SerializedName("fileName")
    private final String fileName;
    
    @SerializedName("required")
    private final boolean required;

    public CurseManifestFile() {
        this(0, 0, "", true);
    }

    public CurseManifestFile(int projectID, int fileID, String fileName, boolean required) {
        this.projectID = projectID;
        this.fileID = fileID;
        this.fileName = fileName;
        this.required = required;
    }

    public int getProjectID() {
        return projectID;
    }

    public int getFileID() {
        return fileID;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public void validate() throws JsonParseException {
        if (projectID == 0 || fileID == 0)
            throw new JsonParseException("Missing Project ID or File ID.");
    }

    public URL getUrl() {
        return NetworkUtils.toURL("https://minecraft.curseforge.com/projects/" + projectID + "/files/" + fileID + "/download");
    }
    
    public CurseManifestFile setFileName(String fileName) {
        return new CurseManifestFile(projectID, fileID, fileName, required);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurseManifestFile that = (CurseManifestFile) o;
        return projectID == that.projectID &&
                fileID == that.fileID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(projectID, fileID);
    }
}
