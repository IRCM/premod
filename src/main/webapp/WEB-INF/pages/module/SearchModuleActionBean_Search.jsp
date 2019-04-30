<%--

    Copyright (c) 2013 Institut de recherches cliniques de Montreal (IRCM)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ include file="/WEB-INF/pages/common/TagLibs.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<stripes:layout-render name="/WEB-INF/pages/layout/layout.jsp">
	<stripes:layout-component name="pageTitle">
		Search Predicted Modules
	</stripes:layout-component>
	<stripes:layout-component name="head">
		<meta name="robots" content="INDEX,NOFOLLOW"/>
		<script type="text/javascript">
			<!--
				// Set descending based on sort order.
				$(function(){
					$("#sort_NAME").change(function(){
						$("#descending").val("false");
					});
					$("#sort_POSITION").change(function(){
						$("#descending").val("false");
					});
					$("#sort_SCORE").change(function(){
						$("#descending").val("true");
					});
					$(":radio[name='sort']:checked").change();
				});
				
				// Focus on text filed beside selected search type.
				$(function(){
					$(":radio[name='searchType'][value='MODULE']").change(function(){
						if (this.checked) {
							$("input[name='moduleNames']").focus();
						}
					});
					$(":radio[name='searchType'][value='UNIT']").change(function(){
						if (this.checked) {
							$("input[name='unitIds']").focus();
						}
					});
					$(":radio[name='searchType'][value='GENE_NAME']").change(function(){
						if (this.checked) {
							$("input[name='geneNames']").focus();
						}
					});
					$(":radio[name='searchType'][value='GENE_ID']").change(function(){
						if (this.checked) {
							$("input[name='geneIds']").focus();
						}
					});
				});
				// Select radio button if text changes beside it.
				$(function(){
					$("input[name='moduleNames']").keypress(function(){
						checkSearch('MODULE');
					});
					$("input[name='moduleNames']").click(function(){
						checkSearch('MODULE');
					});
					$("input[name='unitIds']").keypress(function(){
						checkSearch('UNIT');
					});
					$("input[name='unitIds']").click(function(){
						checkSearch('UNIT');
					});
					$("input[name='geneNames']").keypress(function(){
						checkSearch('GENE_NAME');
					});
					$("input[name='geneNames']").click(function(){
						checkSearch('GENE_NAME');
					});
					$("input[name='geneIds']").keypress(function(){
						checkSearch('GENE_ID');
					});
					$("input[name='geneIds']").click(function(){
						checkSearch('GENE_ID');
					});
				});
				function checkSearch(value) {
					$(":radio[name='searchType'][value='" + value + "']").check();
				}
			//-->
		</script>
		<style>
			h1 {margin:2em 0 1em 0;}
			h2 {margin:1.5em 0 0.5em 0;}
			form div {margin:2em 0;}
			fieldset {margin:2em 0; padding:1em;}
			fieldset h2 {margin:0 0 0.5em 0;}
			fieldset ul {list-style-type:none; padding:0; margin:0 0 0 1em;}
			fieldset li {margin:0.5em 0;}
			fieldset span.version {margin-left:2em;}
			fieldset span.field {display:inline-block; width:17em;}
		</style>
	</stripes:layout-component>
	<stripes:layout-component name="navbar">search</stripes:layout-component>
	<stripes:layout-component name="contents">
		<h1>
			Search for modules
		</h1>
		
		<h2>
			Advanced search
		</h2>
		
		<stripes:form beanclass="ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean">
			<stripes:errors globalErrorsOnly="true"/>
			<stripes:errors field="descending"/>
			<stripes:errors field="page"/>
			<stripes:errors field="unitTag"/>
			<stripes:errors field="geneName"/>
			<stripes:errors field="geneId"/>
			
			<div>
				<stripes:hidden id="descending" name="descending"/>
				<stripes:hidden id="page" name="page"/>
			</div>
			
			<fieldset>
				<h2>Organism</h2>
				<stripes:errors field="organism"/>
				<stripes:errors field="version"/>
				<ul>
					<li>
						<stripes:radio id="organism_HUMAN" name="organism" value="HUMAN"/>
						<label for="organism_HUMAN">
							Human May 2004 assembly (hg17, Build 35)
						</label>
						<span class="version">
							Version:
							<stripes:radio id="version_LATEST" name="version" value="LATEST"/>
							<label for="version_LATEST">
								Latest
							</label>
							<stripes:radio id="version_ARTICLE" name="version" value="ARTICLE"/>
							<label for="version_ARTICLE">
								<a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&amp;cmd=Retrieve&amp;dopt=AbstractPlus&amp;list_uids=16606704&amp;query_hl=1&amp;itool=pubmed_docsum">Article</a>
							</label>
						</span>
					</li>
					<li>
						<stripes:radio id="organism_MOUSE" name="organism" value="MOUSE"/>
						<label for="organism_MOUSE">
							Mouse Mar. 2005 assembly (mm6, Build 34)
						</label>
					</li>
				</ul>
			</fieldset>
			
			<fieldset>
				<h2>Search for</h2>
				<stripes:errors field="searchType"/>
				<ul>
					<li>
						<stripes:radio id="searchType_ALL" name="searchType" value="ALL"/>
						<label for="searchType_ALL">
							All modules
						</label>
					</li>
					<li>
						<stripes:errors field="moduleNames"/>
						<span class="field">
							<stripes:radio id="searchType_MODULE" name="searchType" value="MODULE"/>
							<label for="searchType_MODULE">
								Module name(s)
							</label>
						</span>
						<span class="restriction">
							<stripes:text id="moduleNames" name="moduleNames"/>
							<span class="example">
								(e.g.: mod103457, mod012345)
							</span>
						</span>
					</li>
					<li>
						<stripes:errors field="unitIds"/>
						<span class="field">
							<stripes:radio id="searchType_UNIT" name="searchType" value="UNIT"/>
							<label for="searchType_UNIT">
								Module matrix(ces)
							</label>
						</span>
						<span class="restriction">
							<stripes:text id="unitIds" name="unitIds"/>
							<span class="example">
								(e.g.: M00122, M00191)
							</span>
						</span>
						<br>
						<stripes:errors field="unitConjunction"/>
						<span class="field">
							&nbsp;
						</span>
						<span class="restriction">
							<stripes:radio id="unitConjunction_AND" name="unitConjunction" value="AND"/>
							<label for="unitConjunction_AND">
								And
							</label>
							<stripes:radio id="unitConjunction_OR" name="unitConjunction" value="OR"/>
							<label for="unitConjunction_OR">
								Or (if many matrices)
							</label>
						</span>
						<br>
						<stripes:errors field="unitTag"/>
						<span class="field">
							&nbsp;
						</span>
						<span class="restriction">
							Matrices type:
							<stripes-ext:radio id="unitTag_" name="unitTag" value=""/>
							<label for="unitTag_">
								All
							</label>
							<stripes:radio id="unitTag_TAG" name="unitTag" value="TAG"/>
							<label for="unitTag_TAG">
								Tag only
							</label>
						</span>
					</li>
					<li>
						<stripes:errors field="geneNames"/>
						<span class="field">
							<stripes:radio id="searchType_GENE_NAME" name="searchType" value="GENE_NAME"/>
							<label for="searchType_GENE_NAME">
								Gene name(s)
							</label>
						</span>
						<span class="restriction">
							<stripes:text id="geneNames" name="geneNames"/>
							<span class="example">
								(see <a href="http://www.genenames.org/">HUGO</a>)
							</span>
						</span>
					</li>
					<li>
						<stripes:errors field="geneIds"/>
						<span class="field">
							<stripes:radio id="searchType_GENE_ID" name="searchType" value="GENE_ID"/>
							<label for="searchType_GENE_ID">
								Entrez Gene id(s)
							</label>
						</span>
						<span class="restriction">
							<stripes:text id="geneIds" name="geneIds"/>
							<span class="example">
								(see <a href="http://www.ncbi.nlm.nih.gov/sites/entrez?db=gene">NCBI Entrez Gene</a>)
							</span>
						</span>
					</li>
				</ul>
			</fieldset>
			
			<fieldset>
				<h2>Restrict search to</h2>
				<ul>
					<li>
						<stripes:errors field="chromosomes"/>
						<span class="field">
							<label for="chromosomes">
								Chromosome(s)
							</label>
						</span>
						<span class="restriction">
							<stripes:text id="chromosomes" name="chromosomes"/>
							<span class="example">
								(e.g.: 1, 4, 8 and/or chr1:12345-78901234)
							</span>
						</span>
					</li>
					<li>
						<stripes:errors field="score"/>
						<span class="field">
							<label for="score">
								Module score &gt;=
							</label>
						</span>
						<span class="restriction">
							<stripes:text id="score" name="score"/>
						</span>
					</li>
					<li>
						<stripes:errors field="geneUp"/>
						<stripes:errors field="geneDown"/>
						<span class="field">
							Genomic region around TSSs
						</span>
						<span class="restriction">
							<stripes:text id="geneUp" name="geneUp"/>
							<label for="geneUp">
								Kb upstream
							</label>
							to
							<stripes:text id="geneDown" name="geneDown"/>
							<label for="geneDown">
								Kb downstream
							</label>
						</span>
					</li>
					<li>
						<stripes:errors field="overlapping"/>
						<span class="field">
							CpG island
						</span>
						<span class="restriction">
							<stripes-ext:radio id="overlapping_" name="overlapping" value=""/>
							<label for="overlapping_">
								All
							</label>
							<stripes:radio id="overlapping_OVERLAP" name="overlapping" value="OVERLAP"/>
							<label for="overlapping_OVERLAP">
								Modules must overlap CpG island
							</label>
							<stripes:radio id="overlapping_NO_OVERLAP" name="overlapping" value="NO_OVERLAP"/>
							<label for="overlapping_NO_OVERLAP">
								Modules must not overlap CpG island
							</label>
						</span>
					</li>
				</ul>
			</fieldset>
			
			<fieldset>
				<h2>Output</h2>
				<ul>
					<li>
						<stripes:errors field="sort"/>
						<span class="field">
							Order by
						</span>
						<span class="restriction">
							<stripes:radio id="sort_NAME" name="sort" value="NAME"/>
							<label for="sort_NAME">
								Name
							</label>
							<stripes:radio id="sort_POSITION" name="sort" value="POSITION"/>
							<label for="sort_POSITION">
								Position
							</label>
							<stripes:radio id="sort_SCORE" name="sort" value="SCORE"/>
							<label for="sort_SCORE">
								Score
							</label>
						</span>
					</li>
					<li>
						<stripes:errors field="format"/>
						<span class="field">
							Format
						</span>
						<span class="restriction">
							<stripes:radio id="format_HTML" name="format" value="HTML"/>
							<label for="format_HTML">
								HTML
							</label>
							<stripes:radio id="format_EXCEL" name="format" value="EXCEL"/>
							<label for="format_EXCEL">
								Excel
							</label>
						</span>
					</li>
				</ul>
			</fieldset>
			
			<div class="actions">
				<input type="submit" name="search" value="Search modules"/>
				<input type="reset" name="reset" value="Reset"/>
			</div>
		</stripes:form>
	</stripes:layout-component>
</stripes:layout-render>
