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

package ca.mcgill.genomequebec.premod.persistence.mapper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

/**
 * Converts blob containing locations to a {@link List} of {@link Long}.
 */
public class ExonLocationTypeHandler implements TypeHandler<List<Long>> {
  @Override
  public void setParameter(PreparedStatement ps, int index, List<Long> parameter, JdbcType jdbcType)
      throws SQLException {
    throw new UnsupportedOperationException("Cannot convert exon locations to blob");
  }

  @Override
  public List<Long> getResult(ResultSet rs, int columnIndex) throws SQLException {
    Blob blob = rs.getBlob(columnIndex);
    try {
      return parseBlob(blob);
    } catch (UnsupportedEncodingException e) {
      throw new PersistenceException("Could not convert blob to exon location", e);
    } catch (IOException e) {
      throw new PersistenceException("Could not convert blob to exon location", e);
    }
  }

  @Override
  public List<Long> getResult(ResultSet rs, String columnName) throws SQLException {
    Blob blob = rs.getBlob(columnName);
    try {
      return parseBlob(blob);
    } catch (UnsupportedEncodingException e) {
      throw new PersistenceException("Could not convert blob to exon location", e);
    } catch (IOException e) {
      throw new PersistenceException("Could not convert blob to exon location", e);
    }
  }

  @Override
  public List<Long> getResult(CallableStatement cs, int columnIndex) throws SQLException {
    Blob blob = cs.getBlob(columnIndex);
    try {
      return parseBlob(blob);
    } catch (UnsupportedEncodingException e) {
      throw new PersistenceException("Could not convert blob to exon location", e);
    } catch (IOException e) {
      throw new PersistenceException("Could not convert blob to exon location", e);
    }
  }

  private List<Long> parseBlob(Blob blob) throws SQLException, NumberFormatException, IOException {
    Reader reader = new InputStreamReader(blob.getBinaryStream(), "UTF-8");
    try {
      List<Long> locations = new ArrayList<Long>();
      StringBuilder location = new StringBuilder();
      int read;
      while ((read = reader.read()) != -1) {
        if (read == ',') {
          locations.add(new Long(location.toString()));
          location = new StringBuilder();
        } else {
          location.append((char) read);
        }
      }
      if (location.length() > 0) {
        locations.add(new Long(location.toString()));
      }
      return locations;
    } finally {
      reader.close();
    }
  }
}
