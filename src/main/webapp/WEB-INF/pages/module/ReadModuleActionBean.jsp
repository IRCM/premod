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

<c:set var="module" value="${actionBean.module}"/>
<c:set var="maxUnit" value="<%= ca.mcgill.genomequebec.premod.web.module.ReadModuleActionBean.MAX_ADDITIONNAL_UNITS %>"/>
<stripes:layout-render name="/WEB-INF/pages/layout/layout.jsp">
    <stripes:layout-component name="pageTitle">
        Module
    </stripes:layout-component>
    <stripes:layout-component name="head">
        <meta name="robots" content="NONE"/>
        <script type="text/javascript">
            <!--
                $(function(){
                    zebra();
                });
                
                // Check maximum of ${maxUnit} units.
                var maxUnit = parseInt("${maxUnit}");
                $(function(){
                    $("input[name='units']").change(function(){
                        if ($(this).checked()) {
                            if ($("input[name='units']:checked").length > maxUnit) {
                                $(this).uncheck();
                            }
                        }
                    });
                });
            //-->
        </script>
        <style>
            div.layout_content div {margin:1em 0;}
            h1 {margin:2em 0 1em 0;}
            h2 {margin:1.5em 0 0.5em 0;}
            ul {margin:1em 0;}
            table {margin:0.5em 0;}
        </style>
    </stripes:layout-component>
    <stripes:layout-component name="navbar">search</stripes:layout-component>
    <stripes:layout-component name="contents">
        <h1>
            Module <c:out value="${module.name}"/>
        </h1>
        
        <ul class="other_views">
            <module:ucscUrl var="url" module="${module}"/>
            <li>
                <a href="${url}">
                    Link to UCSC genome Browser
                </a>
            </li>
            <li>
                <a href="#context">
                    Link to genomic context
                </a>
            </li>
            <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleUnitActionBean">
                <stripes:param name="module" value="${module}"/>
            </stripes:url>
            <li>
                <a href="${url}">
                    Link to matrix occurrences
                </a>
            </li>
        </ul>
        
        <stripes:url var="image" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleActionBean" event="showImage">
            <stripes:param name="module" value="${actionBean.module}"/>
            <stripes:param name="units" value="${actionBean.units}"/>
        </stripes:url>
        <div class="image">
            <img src="${image}" width="${actionBean.moduleImage.width}" height="${actionBean.moduleImage.height}" alt="Module image"/>
        </div>
        
        <h2>
            Module information
        </h2>
        <table class="result">
            <tbody>
                <tr>
                    <th>Organism</th>
                    <td>
                        <premod:organism organism="${module.organism}"/>
                    </td>
                </tr>
                <tr>
                    <th>Name</th>
                    <td>
                        <c:out value="${module.name}"/>
                    </td>
                </tr>
                <tr>
                    <th>Type</th>
                    <td>
                        <module:type type="${module.type}"/>
                    </td>
                </tr>
                <tr>
                    <th>Chromosome</th>
                    <td>
                        chr<c:out value="${module.chromosome}"/>:<c:out value="${module.start}"/>-<c:out value="${module.end}"/>
                    </td>
                </tr>
                <tr>
                    <th>Genome Assembly</th>
                    <td>
                        <c:out value="${module.assembly}"/>
                    </td>
                </tr>
                <tr>
                    <th>Score</th>
                    <td>
                        <fmt:formatNumber value="${module.score}" pattern="#"/>
                    </td>
                </tr>
                <tr>
                    <th>Length</th>
                    <td>
                        <c:out value="${module.length}"/>
                    </td>
                </tr>
                <tr>
                    <th>CpG Island</th>
                    <td>
                        <module:cpgIsland cpgIsland="${module.cpgIsland}"/>
                    </td>
                </tr>
            </tbody>
        </table>
        
        <h2>
            Tag Matrices
        </h2>
        <table class="result">
            <thead>
                <tr>
                    <th>Id</th>
                    <th>Name</th>
                    <th>Type</th>
                    <th>Description</th>
                    <th>Nb Occurrence</th>
                    <th>Total Score</th>
                    <th>Other Modules</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="unit" items="${actionBean.bestUnits}">
                    <c:set var="extra" value="${actionBean.unitExtras[unit]}"/>
                    <tr>
                        <premod:transfacUrl var="url" unit="${unit}"/>
                        <td>
                            <a href="${url}">
                                <c:out value="${unit.id}"/>
                            </a>
                        </td>
                        <td><c:out value="${unit.name}"/></td>
                        <td><c:out value="${unit.type}"/></td>
                        <td><c:out value="${unit.description}"/></td>
                        <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleUnitActionBean">
                            <stripes:param name="module" value="${module}"/>
                        </stripes:url>
                        <td>
                            <a href="${url}#A_${unit.id}">
                                <c:out value="${extra.nbOccurrence}"/>
                            </a>
                        </td>
                        <td><c:out value="${extra.minScore}"/></td>
                        <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.unit.ModuleByUnitActionBean">
                            <stripes:param name="unit" value="${unit}"/>
                            <stripes:param name="organism" value="${module.organism}"/>
                            <stripes:param name="version" value="${module.version}"/>
                        </stripes:url>
                        <td>
                            <a href="${url}">
                                <c:out value="${extra.tag}"/>
                            </a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        
        <stripes:form beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleActionBean">
            <stripes:errors/>
            
            <div>
                <stripes:hidden name="module"/>
            </div>
            
            <c:if test="${empty actionBean.nonBestUnits}">
                <h2>
                    No non-tag matrices
                </h2>
            </c:if>
            <c:if test="${!empty actionBean.nonBestUnits}">
                <h2>
                    Other Matrices
                </h2>
                <table class="result">
                    <thead>
                        <tr>
                            <th>&nbsp;</th>
                            <th>Id</th>
                            <th>Name</th>
                            <th>Type</th>
                            <th>Description</th>
                            <th>Nb Occurrence</th>
                            <th>Total Score</th>
                            <th>Other Modules</th>
                        </tr>
                    </thead>
                    <tfoot>
                        <tr>
                            <td colspan="8">
                                <input type="submit" name="show" value="Show selected matrices in module image"/>
                                <span>
                                    (maximum 5 additional matrices)
                                </span>
                            </td>
                        </tr>
                    </tfoot>
                    <tbody>
                        <c:forEach var="unit" items="${actionBean.nonBestUnits}">
                            <c:set var="extra" value="${actionBean.unitExtras[unit]}"/>
                            <tr>
                                <td>
                                    <stripes:checkbox name="units" value="${unit}"/>
                                </td>
                                <premod:transfacUrl var="url" unit="${unit}"/>
                                <td>
                                    <a href="${url}">
                                        <c:out value="${unit.id}"/>
                                    </a>
                                </td>
                                <td><c:out value="${unit.name}"/></td>
                                <td><c:out value="${unit.type}"/></td>
                                <td><c:out value="${unit.description}"/></td>
                                <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleUnitActionBean">
                                    <stripes:param name="module" value="${module}"/>
                                </stripes:url>
                                <td>
                                    <a href="${url}#A_${unit.id}">
                                        <c:out value="${extra.nbOccurrence}"/>
                                    </a>
                                </td>
                                <td><c:out value="${extra.minScore}"/></td>
                                <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.unit.ModuleByUnitActionBean">
                                    <stripes:param name="unit" value="${unit}"/>
                                    <stripes:param name="organism" value="${module.organism}"/>
                                    <stripes:param name="version" value="${module.version}"/>
                                </stripes:url>
                                <td>
                                    <a href="${url}">
                                        <c:out value="${extra.tag}"/>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </stripes:form>
        
        <a name="context"></a>
        <h1>
            Genomic context of module <c:out value="${module.name}"/>
        </h1>
        <stripes:url var="image" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleActionBean" event="showContextImage">
            <stripes:param name="module" value="${actionBean.module}"/>
        </stripes:url>
        <div class="image">
            <img src="${image}" width="${actionBean.contextImage.width}" height="${actionBean.contextImage.height}" alt="Module context" usemap="#context_map"/>
            <map name="context_map">
                <c:forEach var="urlLocation" items="${actionBean.contextImage.urlLocations}">
                    <c:set var="location" value="${urlLocation.location}"/>
                    <area shape="rect" href="${urlLocation.url}" coords="${location.x},${location.y},${location.x+location.width},${location.y+location.height}" alt="${urlLocation.alt}">
                </c:forEach>
            </map>
        </div>
        
        <c:set var="window" value="<%= ca.mcgill.genomequebec.premod.Constants.MODULE_GENE_WINDOW / 1000 %>"/>
        <c:if test="${empty actionBean.nucleotides}">
            <h2>
                No gene found in <c:out value="${window}"/> kb window
            </h2>
        </c:if>
        <c:if test="${!empty actionBean.nucleotides}">
            <h2>
                Surrounding RefSeq genes in <c:out value="${window}"/> kb window
            </h2>
            <table class="result">
                <thead>
                    <tr>
                        <th>mRNA</th>
                        <th>Gene</th>
                        <th>Entrez Gene</th>
                        <th>Strand</th>
                        <th>Start</th>
                        <th>End</th>
                        <th>Relative Position</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="nucleotide" items="${actionBean.nucleotides}">
                        <tr>
                            <premod:nucleotideUrl var="url" mrna="${nucleotide.mrna}"/>
                            <td>
                                <a href="${url}">
                                    <c:out value="${nucleotide.mrna}"/>
                                </a>
                            </td>
                            <td><c:out value="${nucleotide.gene}"/></td>
                            <premod:geneUrl var="url" geneId="${nucleotide.locusLink}"/>
                            <td>
                                <a href="${url}">
                                    <c:out value="${nucleotide.locusLink}"/>
                                </a>
                            </td>
                            <td><c:out value="${nucleotide.strand}"/></td>
                            <td><c:out value="${nucleotide.start}"/></td>
                            <td><c:out value="${nucleotide.end}"/></td>
                            <td><c:out value="${actionBean.nucleotidesRelativePosition[nucleotide]}"/></td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <c:if test="${empty actionBean.modules}">
            <h2>
                No modules found in <c:out value="${window}"/> kb window
            </h2>
        </c:if>
        <c:if test="${!empty actionBean.modules}">
            <h2>
                Surrounding modules in <c:out value="${window}"/> kb window
            </h2>
            <table class="result">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>Chromosome</th>
                        <th>Score</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="module" items="${actionBean.modules}">
                        <tr>
                            <td>
                                <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadModuleActionBean">
                                    <stripes:param name="module" value="${module}"/>
                                </stripes:url>
                                <a href="${url}">
                                    <c:out value="${module.name}"/>
                                </a>
                            </td>
                            <td>
                                chr<c:out value="${module.chromosome}"/>:<c:out value="${module.start}"/>-<c:out value="${module.end}"/>
                            </td>
                            <td>
                                <fmt:formatNumber value="${module.score}" pattern="#"/>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
        
        <h2>
            Module Sequence
        </h2>
        <table class="result">
            <tbody>
                <tr>
                    <td><pre><premod:sequencePre sequenceName="${actionBean.sequenceName}" sequence="${actionBean.sequence}"/></pre></td>
                </tr>
            </tbody>
        </table>
    </stripes:layout-component>
</stripes:layout-render>
