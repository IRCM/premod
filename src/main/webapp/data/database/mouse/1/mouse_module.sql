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

DROP TABLE IF EXISTS `module_mouse`;
CREATE TABLE  `module_mouse` (
  `id` char(18) NOT NULL default '0',
  `type` enum('Predicted') default 'Predicted',
  `chromosome` enum('NA','0','1','2','3','4','5','6','7','8','9','10','11','12','13','14','15','16','17','18','19','M','Un','X','Y') NOT NULL default 'NA',
  `start` int(10) unsigned NOT NULL default '0',
  `end` int(10) unsigned NOT NULL default '0',
  `length` int(10) unsigned NOT NULL default '0',
  `assembly` char(13) NOT NULL default '',
  `score` double default NULL,
  `downstream_gene` int(11) default NULL,
  `downstream_gene_locus` char(15) default '',
  `downstream_gene_name` char(30) default '',
  `upstream_gene` int(11) default NULL,
  `upstream_gene_locus` char(15) default '',
  `upstream_gene_name` char(30) default '',
  `cpg_island` enum('-','0','1') default '-',
  PRIMARY KEY  (`id`),
  KEY `score` (`score`),
  KEY `cpg_island` (`cpg_island`),
  KEY `downstream_gene` (`downstream_gene`),
  KEY `upstream_gene` (`upstream_gene`),
  KEY `chromosome` (`chromosome`,`start`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;
