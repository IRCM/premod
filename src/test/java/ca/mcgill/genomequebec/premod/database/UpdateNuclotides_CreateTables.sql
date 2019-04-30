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

DROP TABLE IF EXISTS refflat_human;
DROP TABLE IF EXISTS refflat_mouse;
DROP TABLE IF EXISTS gene_info;
DROP TABLE IF EXISTS gene2accession;

-- Human RefFlat.
CREATE TABLE refflat_human (
  geneName varchar(255) NOT NULL DEFAULT '',
  name varchar(255) NOT NULL DEFAULT '',
  chrom varchar(255) NOT NULL DEFAULT '',
  strand char(1) NOT NULL DEFAULT '',
  txStart int(10) unsigned NOT NULL DEFAULT '0',
  txEnd int(10) unsigned NOT NULL DEFAULT '0',
  cdsStart int(10) unsigned NOT NULL DEFAULT '0',
  cdsEnd int(10) unsigned NOT NULL DEFAULT '0',
  exonCount int(10) unsigned NOT NULL DEFAULT '0',
  exonStarts longblob NOT NULL,
  exonEnds longblob NOT NULL,
  KEY start (txStart),
  KEY chrom_2 (chrom(12),txEnd),
  KEY name (name(10)),
  KEY chrom_strand (chrom(12),strand,txStart),
  KEY end (txEnd),
  KEY chrom_strand_2 (chrom(12),strand,txEnd),
  KEY geneName (geneName(10)),
  KEY chrom (chrom(12),txStart)
) ENGINE=MyISAM;
-- Mouse RefFlat.
CREATE TABLE refflat_mouse (
  geneName varchar(255) NOT NULL DEFAULT '',
  name varchar(255) NOT NULL DEFAULT '',
  chrom varchar(255) NOT NULL DEFAULT '',
  strand char(1) NOT NULL DEFAULT '',
  txStart int(10) unsigned NOT NULL DEFAULT '0',
  txEnd int(10) unsigned NOT NULL DEFAULT '0',
  cdsStart int(10) unsigned NOT NULL DEFAULT '0',
  cdsEnd int(10) unsigned NOT NULL DEFAULT '0',
  exonCount int(10) unsigned NOT NULL DEFAULT '0',
  exonStarts longblob NOT NULL,
  exonEnds longblob NOT NULL,
  KEY start (txStart),
  KEY chrom_2 (chrom(12),txEnd),
  KEY name (name(10)),
  KEY chrom_strand (chrom(12),strand,txStart),
  KEY end (txEnd),
  KEY chrom_strand_2 (chrom(12),strand,txEnd),
  KEY geneName (geneName(10)),
  KEY chrom (chrom(12),txStart)
) ENGINE=MyISAM;
-- Gene info.
CREATE TABLE gene_info (
  tax_id int(11) NOT NULL,
  gene_id int(11) NOT NULL,
  symbol varchar(255) DEFAULT NULL,
  locus_tag varchar(255) DEFAULT NULL,
  synonyms varchar(255) DEFAULT NULL,
  dbxrefs varchar(255) DEFAULT NULL,
  chromosome varchar(255) DEFAULT NULL,
  map_location varchar(255) DEFAULT NULL,
  description varchar(255) DEFAULT NULL,
  type_of_gene varchar(255) DEFAULT NULL,
  symbol_nomenclature_authority varchar(255) DEFAULT NULL,
  full_name_nomenclature_authority varchar(255) DEFAULT NULL,
  nomenclature_status varchar(255) DEFAULT NULL,
  other_designations varchar(255) DEFAULT NULL,
  modification_date date DEFAULT NULL,
  KEY tax_gene (tax_id,symbol)
) ENGINE=MyISAM;
-- Gene to accession.
CREATE TABLE gene2accession (
  tax_id int(11) NOT NULL,
  gene_id int(11) NOT NULL,
  status varchar(255) DEFAULT NULL,
  nucleotide_accession varchar(255) DEFAULT NULL,
  nucleotide_gi int(11) DEFAULT NULL,
  protein_accession varchar(255) DEFAULT NULL,
  protein_gi int(11) DEFAULT NULL,
  gnucleotide_accession varchar(255) DEFAULT NULL,
  gnucleotide_gi int(11) DEFAULT NULL,
  start bigint(20) DEFAULT NULL,
  end bigint(20) DEFAULT NULL,
  orientation enum('+','-') DEFAULT NULL,
  assembly varchar(255) DEFAULT NULL,
  KEY tax_nucleotide (tax_id, nucleotide_accession)
) ENGINE=MyISAM;
