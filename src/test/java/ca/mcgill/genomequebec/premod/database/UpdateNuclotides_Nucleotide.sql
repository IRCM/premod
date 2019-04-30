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

-- Insert nucleotide based on gene2accession.
DELETE FROM nucleotide;

INSERT INTO nucleotide(organism, mrna, chromosome, strand, start, end, codingStart, codingEnd, gene, locusLink, exonCount, exonStarts, exonEnds)
SELECT 'HUMAN' AS organism, refFlat.name AS mrna, SUBSTRING(refFlat.chrom, 4) AS chromosome, refFlat.strand AS strand, refFlat.txStart AS start, refFlat.txEnd as end, refFlat.cdsStart AS codingStart, refFlat.cdsEnd AS codingEnd, refFlat.geneName AS gene, MIN(gene2accession.gene_id) AS locusLink, refFlat.exonCount AS exonCount, refFlat.exonStarts AS exonStarts, refFlat.exonEnds AS exonEnds
FROM refflat_human AS refFlat
LEFT JOIN gene2accession ON gene2accession.tax_id = 9606 AND gene2accession.nucleotide_accession = refFlat.name
GROUP BY refFlat.name, refFlat.chrom, refFlat.strand, refFlat.txStart, refFlat.txEnd, refFlat.cdsStart, refFlat.cdsEnd, refFlat.geneName, refFlat.exonCount, refFlat.exonStarts, refFlat.exonEnds;

INSERT INTO nucleotide(organism, mrna, chromosome, strand, start, end, codingStart, codingEnd, gene, locusLink, exonCount, exonStarts, exonEnds)
SELECT 'MOUSE' AS organism, refFlat.name AS mrna, SUBSTRING(refFlat.chrom, 4) AS chromosome, refFlat.strand AS strand, refFlat.txStart AS start, refFlat.txEnd as end, refFlat.cdsStart AS codingStart, refFlat.cdsEnd AS codingEnd, refFlat.geneName AS gene, MIN(gene2accession.gene_id) AS locusLink, refFlat.exonCount AS exonCount, refFlat.exonStarts AS exonStarts, refFlat.exonEnds AS exonEnds
FROM refflat_mouse AS refFlat
LEFT JOIN gene2accession ON gene2accession.tax_id = 10090 AND gene2accession.nucleotide_accession = refFlat.name
GROUP BY refFlat.name, refFlat.chrom, refFlat.strand, refFlat.txStart, refFlat.txEnd, refFlat.cdsStart, refFlat.cdsEnd, refFlat.geneName, refFlat.exonCount, refFlat.exonStarts, refFlat.exonEnds;

-- Update missing gene ids using gene_info's symbol column.
DROP TEMPORARY TABLE IF EXISTS _nucleotide;
CREATE TEMPORARY TABLE _nucleotide (
organism ENUM('HUMAN','MOUSE') NOT NULL,
gene varchar(255) NOT NULL,
locusLink int(10) unsigned DEFAULT NULL,
KEY gene (organism,gene)
);
INSERT INTO _nucleotide (gene, locusLink)
SELECT nucleotide.gene, MIN(gene_info.gene_id)
FROM nucleotide
JOIN gene_info
WHERE gene_info.tax_id = 9606
AND nucleotide.organism = 'HUMAN'
AND nucleotide.locusLink IS NULL
AND gene_info.symbol = nucleotide.gene
GROUP BY nucleotide.gene;
UPDATE nucleotide
JOIN _nucleotide ON _nucleotide.gene = nucleotide.gene
SET nucleotide.locusLink = _nucleotide.locusLink
WHERE nucleotide.organism = 'HUMAN'
AND nucleotide.locusLink IS NULL;
DELETE FROM _nucleotide;
INSERT INTO _nucleotide (gene, locusLink)
SELECT nucleotide.gene, MIN(gene_info.gene_id)
FROM nucleotide
JOIN gene_info
WHERE gene_info.tax_id = 10090
AND nucleotide.organism = 'MOUSE'
AND nucleotide.locusLink IS NULL
AND gene_info.symbol = nucleotide.gene
GROUP BY nucleotide.gene;
UPDATE nucleotide
JOIN _nucleotide ON _nucleotide.gene = nucleotide.gene
SET nucleotide.locusLink = _nucleotide.locusLink
WHERE nucleotide.organism = 'MOUSE'
AND nucleotide.locusLink IS NULL;
DELETE FROM _nucleotide;

-- Update missing gene ids using gene_info's synonyms column.
INSERT INTO _nucleotide (gene, locusLink)
SELECT nucleotide.gene, MIN(gene_info.gene_id)
FROM nucleotide
JOIN gene_info
WHERE gene_info.tax_id = 9606
AND nucleotide.organism = 'HUMAN'
AND nucleotide.locusLink IS NULL
AND gene_info.synonyms REGEXP CONCAT('^(.*\\|)?', nucleotide.gene, '(\\|.*)?$')
GROUP BY nucleotide.gene;
UPDATE nucleotide
JOIN _nucleotide ON _nucleotide.gene = nucleotide.gene
SET nucleotide.locusLink = _nucleotide.locusLink
WHERE nucleotide.organism = 'HUMAN'
AND nucleotide.locusLink IS NULL;
DELETE FROM _nucleotide;
INSERT INTO _nucleotide (gene, locusLink)
SELECT nucleotide.gene, MIN(gene_info.gene_id)
FROM nucleotide
JOIN gene_info
WHERE gene_info.tax_id = 10090
AND nucleotide.organism = 'MOUSE'
AND nucleotide.locusLink IS NULL
AND gene_info.synonyms REGEXP CONCAT('^(.*\\|)?', nucleotide.gene, '(\\|.*)?$')
GROUP BY nucleotide.gene;
UPDATE nucleotide
JOIN _nucleotide ON _nucleotide.gene = nucleotide.gene
SET nucleotide.locusLink = _nucleotide.locusLink
WHERE nucleotide.organism = 'MOUSE'
AND nucleotide.locusLink IS NULL;
DELETE FROM _nucleotide;

DROP TEMPORARY TABLE _nucleotide;
