/*
 * Copyright (c) 2013 Institut de recherches cliniques de Montreal (IRCM)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ca.mcgill.genomequebec.premod.business;

import java.io.Serializable;

/**
 * DNA matrix (usually TransFac matrix).
 */
public class Unit implements Serializable {
  /**
   * Matrix is taken from TransFac.
   */
  public static final String TRANSFAC_TYPE = "TransFac";
  /**
   * Matrix is taken from TRRD.
   */
  public static final String TRRD_TYPE = "TRRD";

  /**
   * Tag types for unit.
   */
  public static enum TagType {
    TAG, NO_TAG
  }

  private static final long serialVersionUID = -5409061680908223593L;

  /**
   * Identifier of this matrix.
   */
  private String id;
  /**
   * Where this matrix was taken.
   */
  private String type;
  /**
   * Matrix name.
   */
  private String name;
  /**
   * Matrix description.
   */
  private String description;
  /**
   * Length of the Module.
   */
  private Long length;

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Unit other = (Unit) obj;
    if (id == null) {
      if (other.id != null) {
        return false;
      }
    } else if (!id.equals(other.id)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Unit_" + this.getId();
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Long getLength() {
    return length;
  }

  public void setLength(Long length) {
    this.length = length;
  }
}
