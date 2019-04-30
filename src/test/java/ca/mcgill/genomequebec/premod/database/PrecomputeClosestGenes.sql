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

-- Prepare update.
DROP TABLE IF EXISTS _modules;
DROP TABLE IF EXISTS _modules2;
CREATE TABLE _modules (
id BIGINT UNSIGNED,
organism ENUM('HUMAN','MOUSE'),
chromosome VARCHAR(20),
middle BIGINT,
down_plus_start BIGINT,
down_minus_end BIGINT,
up_plus_start BIGINT,
up_minus_end BIGINT,
down_plus_position BIGINT,
down_minus_position BIGINT,
up_plus_position BIGINT,
up_minus_position BIGINT,
down_gene INT(11),
down_gene_locus INT(10),
down_gene_name VARCHAR(30),
up_gene INT(11),
up_gene_locus INT(10),
up_gene_name VARCHAR(30),
INDEX (id),
INDEX middle (organism, chromosome(12), middle)
);
CREATE TABLE _modules2 (
id BIGINT UNSIGNED,
value BIGINT,
INDEX (id)
);
INSERT INTO _modules (id, organism, chromosome, middle)
SELECT id, organism, chromosome, start + ((end - start) / 2)
FROM module;

-- Find max start for upstream gene.
INSERT INTO _modules2 (id, value)
SELECT _modules.id, MAX(nucleotide.start)
FROM _modules
JOIN nucleotide ON nucleotide.organism = _modules.organism AND nucleotide.chromosome = _modules.chromosome
WHERE nucleotide.start <= _modules.middle
AND nucleotide.strand = '+'
GROUP BY _modules.id;
UPDATE _modules
JOIN _modules2 ON _modules2.id = _modules.id
SET _modules.up_plus_start = _modules2.value;
DELETE FROM _modules2;

-- Find min end for upstream gene.
INSERT INTO _modules2 (id, value)
SELECT _modules.id, MIN(nucleotide.end)
FROM _modules
JOIN nucleotide ON nucleotide.organism = _modules.organism AND nucleotide.chromosome = _modules.chromosome
WHERE nucleotide.end >= _modules.middle
AND nucleotide.strand = '-'
GROUP BY _modules.id;
UPDATE _modules
JOIN _modules2 ON _modules2.id = _modules.id
SET _modules.up_plus_start = _modules2.value;
DELETE FROM _modules2;

-- Find min start for downstream gene.
INSERT INTO _modules2 (id, value)
SELECT _modules.id, MIN(nucleotide.start)
FROM _modules
JOIN nucleotide ON nucleotide.organism = _modules.organism AND nucleotide.chromosome = _modules.chromosome
WHERE nucleotide.start >= _modules.middle
AND nucleotide.strand = '+'
GROUP BY _modules.id;
UPDATE _modules
JOIN _modules2 ON _modules2.id = _modules.id
SET _modules.up_plus_start = _modules2.value;
DELETE FROM _modules2;

-- Find max end for downstream gene.
INSERT INTO _modules2 (id, value)
SELECT _modules.id, MAX(nucleotide.end)
FROM _modules
JOIN nucleotide ON nucleotide.organism = _modules.organism AND nucleotide.chromosome = _modules.chromosome
WHERE nucleotide.end <= _modules.middle
AND nucleotide.strand = '-'
GROUP BY _modules.id;
UPDATE _modules
JOIN _modules2 ON _modules2.id = _modules.id
SET _modules.up_plus_start = _modules2.value;
DELETE FROM _modules2;

-- Find relative positions.
UPDATE _modules
SET down_plus_position = middle - down_plus_start,
down_minus_position = down_minus_end - middle,
up_plus_position = middle - up_plus_start,
up_minus_position = up_minus_end - middle;

-- Find downstream gene.
UPDATE _modules
JOIN nucleotide ON nucleotide.organism = _modules.organism AND nucleotide.chromosome = _modules.chromosome AND nucleotide.start = _modules.down_plus_start
SET _modules.down_gene = down_plus_position,
down_gene_locus = nucleotide.locusLink,
down_gene_name = nucleotide.gene
WHERE down_plus_position < down_minus_position
OR (down_plus_position IS NOT NULL AND down_minus_position IS NULL);
UPDATE _modules
JOIN nucleotide ON nucleotide.organism = _modules.organism AND nucleotide.chromosome = _modules.chromosome AND nucleotide.end = _modules.down_minus_end
SET _modules.down_gene = down_minus_position,
down_gene_locus = nucleotide.locusLink,
down_gene_name = nucleotide.gene
WHERE down_plus_position >= down_minus_position
OR (down_plus_position IS NULL AND down_minus_position IS NOT NULL);
-- Find upstream gene.
UPDATE _modules
JOIN nucleotide ON nucleotide.organism = _modules.organism AND nucleotide.chromosome = _modules.chromosome AND nucleotide.start = _modules.up_plus_start
SET _modules.down_gene = up_plus_position,
down_gene_locus = nucleotide.locusLink,
down_gene_name = nucleotide.gene
WHERE up_plus_position < up_minus_position
OR (up_plus_position IS NOT NULL AND up_minus_position IS NULL);
UPDATE _modules
JOIN nucleotide ON nucleotide.organism = _modules.organism AND nucleotide.chromosome = _modules.chromosome AND nucleotide.end = _modules.up_minus_end
SET _modules.down_gene = up_minus_position,
down_gene_locus = nucleotide.locusLink,
down_gene_name = nucleotide.gene
WHERE up_plus_position >= up_minus_position
OR (up_plus_position IS NULL AND up_minus_position IS NOT NULL);

-- DROP TEMPORARY TABLE _modules;
-- DROP TEMPORARY TABLE _modules2;
