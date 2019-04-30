--
-- Copyright (c) 2013 Institut de recherches cliniques de Montreal (IRCM)
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU Affero General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU Affero General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.
--

DROP TABLE IF EXISTS `unit_mouse`;
CREATE TABLE  `unit_mouse` (
  `id` char(6) NOT NULL default '',
  `name` char(50) NOT NULL default '',
  `description` char(255) default NULL,
  `tag` int(11) default NULL,
  `notag` int(11) default NULL,
  `any` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
