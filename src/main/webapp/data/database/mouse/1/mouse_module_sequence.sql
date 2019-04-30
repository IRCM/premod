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

DROP TABLE IF EXISTS `sequence_mouse`;
CREATE TABLE  `sequence_mouse` (
  `module_id` varchar(18) NOT NULL default '0',
  `sequence` text NOT NULL,
  PRIMARY KEY  (`module_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;