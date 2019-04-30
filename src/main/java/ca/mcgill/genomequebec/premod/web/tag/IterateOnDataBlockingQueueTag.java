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

package ca.mcgill.genomequebec.premod.web.tag;

import ca.mcgill.genomequebec.premod.service.DataBlockingQueue;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.jstl.core.LoopTagSupport;

/**
 * Returns alternate class based on element index.
 */
public class IterateOnDataBlockingQueueTag extends LoopTagSupport {
  private static final long serialVersionUID = -3275762304766430295L;
  private DataBlockingQueue<?> queue;
  private Object items;

  @Override
  protected Object next() throws JspTagException {
    queue.waitForResults();
    return queue.poll();
  }

  @Override
  protected boolean hasNext() throws JspTagException {
    queue.waitForResults();
    return queue.peek() != null;
  }

  @Override
  protected void prepare() throws JspTagException {
    if (queue == null) {
      throw new JspTagException("items not of type " + DataBlockingQueue.class.getSimpleName());
    }
  }

  @Override
  public void release() {
    super.release();
    queue = null;
    items = null;
  }

  public final Object getItems() {
    return items;
  }

  /**
   * Sets items to iterate over.
   * 
   * @param items
   *          items
   */
  public final void setItems(Object items) {
    this.items = items;
    if (items instanceof DataBlockingQueue) {
      queue = (DataBlockingQueue<?>) items;
    }
  }
}
