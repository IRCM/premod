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

DROP TABLE IF EXISTS `unit_occurrence_human`;
CREATE TABLE  `unit_occurrence_human` (
  `unit_id` char(10) NOT NULL default '',
  `chromosome` enum('NA','0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','20','21','22','X','Y') NOT NULL default 'NA',
  `start` int(10) unsigned NOT NULL default '0',
  `end` int(10) unsigned NOT NULL default '0',
  `strand` enum('+','-') NOT NULL default '+',
  `assembly` char(13) NOT NULL default '',
  `occ_score` double default NULL,
  `total_score` double default NULL,
  `module_id` char(18) NOT NULL default '0',
  `tag_number` enum('0','1','2','3','4','5') default '0',
  PRIMARY KEY  (`unit_id`,`chromosome`,`start`,`strand`),
  KEY `end` (`end`),
  KEY `unit_id` (`unit_id`),
  KEY `unit_module` (`unit_id`,`module_id`),
) ENGINE=MyISAM DEFAULT CHARSET=latin1;