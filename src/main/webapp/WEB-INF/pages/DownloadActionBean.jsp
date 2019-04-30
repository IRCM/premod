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

<c:set var="organism" value="${actionBean.organism}"/>
<c:set var="version" value="${actionBean.version}"/>
<stripes:layout-render name="/WEB-INF/pages/layout/layout.jsp">
    <stripes:layout-component name="pageTitle">
        Download
    </stripes:layout-component>
    <stripes:layout-component name="head">
        <meta name="robots" content="ALL"/>
        <style>
            h1 {margin:1.5em 0;}
            ul.other_views {margin:1em 0;}
            h2 {margin:2em 0 0 0;}
            ul.database, ul.other {margin:0 0 0 3em; padding:0;}
            ul.database li, ul.other li {margin:0.5em 0; padding:0;}
            ul.database ul, ul.other ul {margin:0 0 0 3em; padding:0;}
            ul.database ul li, ul.other ul li {margin:0; padding:0; position:relative; height:1.1em; width:20em;}
            ul.database ul li span.size, ul.other ul li span.size {position:absolute; top:0; left:21em; width:15em;}
        </style>
    </stripes:layout-component>
    <stripes:layout-component name="navbar">download</stripes:layout-component>
    <stripes:layout-component name="contents">
        <fmt:bundle basename="${fmtBundle}">
        
        <h1>
            <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                Human downloads Version 1.0 (latest)
            </c:if>
            <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                Human downloads Version 0.0 (<a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&amp;cmd=Retrieve&amp;dopt=AbstractPlus&amp;list_uids=16606704&amp;query_hl=1&amp;itool=pubmed_docsum">Blanchette et al.</a>)
            </c:if>
            <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                Mouse downloads Version 1.0 (latest)
            </c:if>
        </h1>
        
        <ul class="other_views">
            <c:if test="${organism != 'HUMAN' || version != 'LATEST'}">
                <li>
                    <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.DownloadActionBean">
                        <stripes:param name="organism" value="HUMAN"/>
                        <stripes:param name="version" value="LATEST"/>
                    </stripes:url>
                    <a href="${url}">
                        Link to human downloads Version 1.0 (latest)
                    </a>
                </li>
            </c:if>
            <c:if test="${organism != 'HUMAN' || version != 'ARTICLE'}">
                <li>
                    <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.DownloadActionBean">
                        <stripes:param name="organism" value="HUMAN"/>
                        <stripes:param name="version" value="ARTICLE"/>
                    </stripes:url>
                    <a href="${url}">
                        Link to human downloads Version 0.0
                    </a>
                    (<a href="http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=pubmed&amp;cmd=Retrieve&amp;dopt=AbstractPlus&amp;list_uids=16606704&amp;query_hl=1&amp;itool=pubmed_docsum">Blanchette et al.</a>)
                </li>
            </c:if>
            <c:if test="${organism != 'MOUSE' || version != 'LATEST'}">
                <li>
                    <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.DownloadActionBean">
                        <stripes:param name="organism" value="MOUSE"/>
                        <stripes:param name="version" value="LATEST"/>
                    </stripes:url>
                    <a href="${url}">
                        Link to mouse downloads Version 1.0 (latest)
                    </a>
                </li>
            </c:if>
        </ul>
        
        <h2>
            Database download
        </h2>
        <ul class="database">
            <li>
                <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                    <c:url var="url" value="/data/database/human/1/human_module.sql"/>
                </c:if>
                <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                    <c:url var="url" value="/data/database/human/0/human_module.sql"/>
                </c:if>
                <c:if test="${organism == 'MOUSE'}">
                    <c:url var="url" value="/data/database/mouse/1/mouse_module.sql"/>
                </c:if>
                <a href="${url}">
                    Module
                </a>
                <ul>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/database/human/1/human_module_database.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/database/human/0/human_module_database.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE'}">
                            <c:url var="url" value="/data/database/mouse/1/mouse_module_database.txt.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="database.module.name.${organism}.${version}"/>
                        </a>
                        <span class="size">
                            <fmt:message key="database.module.size.${organism}.${version}"/>
                        </span>
                    </li>
                </ul>   
            </li>
            <li>
                <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                    <c:url var="url" value="/data/database/human/1/human_module_sequence.sql"/>
                </c:if>
                <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                    <c:url var="url" value="/data/database/human/0/human_module_sequence.sql"/>
                </c:if>
                <c:if test="${organism == 'MOUSE'}">
                    <c:url var="url" value="/data/database/mouse/1/mouse_module_sequence.sql"/>
                </c:if>
                <a href="${url}">
                    Module sequences
                </a>
                <ul>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/database/human/1/human_module_sequence_database.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/database/human/0/human_module_sequence_database.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE'}">
                            <c:url var="url" value="/data/database/mouse/1/mouse_module_sequence_database.txt.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="database.moduleSequence.name.${organism}.${version}"/>
                        </a>
                        <span class="size">
                            <fmt:message key="database.moduleSequence.size.${organism}.${version}"/>
                        </span>
                    </li>
                </ul>   
            </li>
            <li>
                <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                    <c:url var="url" value="/data/database/human/1/human_matrix.sql"/>
                </c:if>
                <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                    <c:url var="url" value="/data/database/human/0/human_matrix.sql"/>
                </c:if>
                <c:if test="${organism == 'MOUSE'}">
                    <c:url var="url" value="/data/database/mouse/1/mouse_matrix.sql"/>
                </c:if>
                <a href="${url}">
                    Matrix
                </a>
                <ul>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/database/human/1/human_matrix_database.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/database/human/0/human_matrix_database.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE'}">
                            <c:url var="url" value="/data/database/mouse/1/mouse_matrix_database.txt.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="database.unit.name.${organism}.${version}"/>
                        </a>
                        <span class="size">
                            <fmt:message key="database.unit.size.${organism}.${version}"/>
                        </span>
                    </li>
                </ul>   
            </li>
            <li>
                <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                    <c:url var="url" value="/data/database/human/1/human_matrix_occurence.sql"/>
                </c:if>
                <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                    <c:url var="url" value="/data/database/human/0/human_matrix_occurence.sql"/>
                </c:if>
                <c:if test="${organism == 'MOUSE'}">
                    <c:url var="url" value="/data/database/mouse/1/mouse_matrix_occurence.sql"/>
                </c:if>
                <a href="${url}">
                    Matrix occurrence
                </a>
                <ul>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/database/human/1/human_matrix_occurence_database.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/database/human/0/human_matrix_occurence_database.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE'}">
                            <c:url var="url" value="/data/database/mouse/1/mouse_matrix_occurence_database.txt.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="database.unitOccurrence.name.${organism}.${version}"/>
                        </a>
                        <span class="size">
                            <fmt:message key="database.unitOccurrence.size.${organism}.${version}"/>
                        </span>
                    </li>
                </ul>   
            </li>
        </ul>
        
        <h2>
            Other
        </h2>
        <ul class="other">
            <li>
                All modules (comma delimited csv)
                <ul>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/module/human/1/human_module_comma.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/module/human/0/human_module_comma.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                            <c:url var="url" value="/data/module/mouse/1/mouse_module_comma.txt.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="other.module.comma.name.${organism}.${version}"/>
                        </a>
                        <span class="size">
                            <fmt:message key="other.module.comma.size.${organism}.${version}"/>
                        </span>
                    </li>
                </ul>   
            </li>
            <li>
                All modules (tab delimited csv)
                <ul>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/module/human/1/human_module_tab.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/module/human/0/human_module_tab.txt.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                            <c:url var="url" value="/data/module/mouse/1/mouse_module_tab.txt.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="other.module.tab.name.${organism}.${version}"/>
                        </a>
                        <span class="size">
                            <fmt:message key="other.module.tab.size.${organism}.${version}"/>
                        </span>
                    </li>
                </ul>   
            </li>
            <li>
                Module DNA Sequence in Fasta Format (gzip or winzip required)
                <ul>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/sequence/human/1/all.fas.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/sequence/human/0/all.fas.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                            <c:url var="url" value="/data/sequence/mouse/1/all.fas.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="other.sequence.all.name.${organism}.${version}"/>
                        </a>
                        <span class="size">
                            <fmt:message key="other.sequence.all.size.${organism}.${version}"/>
                        </span>
                    </li>
                    <c:if test="${organism == 'HUMAN'}">
                        <c:set var="begin" value="1"/>
                        <c:set var="end" value="22"/>
                    </c:if>
                    <c:if test="${organism == 'MOUSE'}">
                        <c:set var="begin" value="1"/>
                        <c:set var="end" value="19"/>
                    </c:if>
                    <c:forEach var="chromosome" begin="${begin}" end="${end}">
                        <li>
                            <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                                <c:url var="url" value="/data/sequence/human/1/chr${chromosome}.fas.gz"/>
                            </c:if>
                            <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                                <c:url var="url" value="/data/sequence/human/0/chr${chromosome}.fas.gz"/>
                            </c:if>
                            <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                                <c:url var="url" value="/data/sequence/mouse/1/chr${chromosome}.fas.gz"/>
                            </c:if>
                            <a href="${url}">
                                <fmt:message key="other.sequence.chromosome.name.${organism}.${version}">
                                    <fmt:param value="${chromosome}"/>
                                </fmt:message>
                            </a>
                            <span class="size">
                                <fmt:message key="other.sequence.chromosome.size.${organism}.${version}.${chromosome}"/>
                            </span>
                        </li>
                    </c:forEach>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/sequence/human/1/chrX.fas.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/sequence/human/0/chrX.fas.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                            <c:url var="url" value="/data/sequence/mouse/1/chrX.fas.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="other.sequence.chromosome.name.${organism}.${version}">
                                <fmt:param value="X"/>
                            </fmt:message>
                        </a>
                        <span class="size">
                            <fmt:message key="other.sequence.chromosome.size.${organism}.${version}.X"/>
                        </span>
                    </li>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/sequence/human/1/chrY.fas.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/sequence/human/0/chrY.fas.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                            <c:url var="url" value="/data/sequence/mouse/1/chrY.fas.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="other.sequence.chromosome.name.${organism}.${version}">
                                <fmt:param value="Y"/>
                            </fmt:message>
                        </a>
                        <span class="size">
                            <fmt:message key="other.sequence.chromosome.size.${organism}.${version}.Y"/>
                        </span>
                    </li>
                </ul>   
            </li>
            <li>
                Module tracks for UCSC genome browser (<a href="http://www.genome.ucsc.edu/cgi-bin/hgGateway">http://www.genome.ucsc.edu/cgi-bin/hgGateway</a>) (gzip or winzip required)
                <br>
                To view the modules in the UCSC genome browser, upload the following files in the browser 
                <ul>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/UCSC/human/1/all.gff.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/UCSC/human/0/all.gff.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                            <c:url var="url" value="/data/UCSC/mouse/1/all.gff.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="other.ucsc.all.name.${organism}.${version}"/>
                        </a>
                        <span class="size">
                            <fmt:message key="other.ucsc.all.size.${organism}.${version}"/>
                        </span>
                    </li>
                    <c:if test="${organism == 'HUMAN'}">
                        <c:set var="begin" value="1"/>
                        <c:set var="end" value="22"/>
                    </c:if>
                    <c:if test="${organism == 'MOUSE'}">
                        <c:set var="begin" value="1"/>
                        <c:set var="end" value="19"/>
                    </c:if>
                    <c:forEach var="chromosome" begin="${begin}" end="${end}">
                        <li>
                            <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                                <c:url var="url" value="/data/UCSC/human/1/chr${chromosome}.gff.gz"/>
                            </c:if>
                            <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                                <c:url var="url" value="/data/UCSC/human/0/chr${chromosome}.gff.gz"/>
                            </c:if>
                            <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                                <c:url var="url" value="/data/UCSC/mouse/1/chr${chromosome}.gff.gz"/>
                            </c:if>
                            <a href="${url}">
                                <fmt:message key="other.ucsc.chromosome.name.${organism}.${version}">
                                    <fmt:param value="${chromosome}"/>
                                </fmt:message>
                            </a>
                            <span class="size">
                                <fmt:message key="other.ucsc.chromosome.size.${organism}.${version}.${chromosome}"/>
                            </span>
                        </li>
                    </c:forEach>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/UCSC/human/1/chrX.gff.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/UCSC/human/0/chrX.gff.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                            <c:url var="url" value="/data/UCSC/mouse/1/chrX.gff.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="other.ucsc.chromosome.name.${organism}.${version}">
                                <fmt:param value="X"/>
                            </fmt:message>
                        </a>
                        <span class="size">
                            <fmt:message key="other.ucsc.chromosome.size.${organism}.${version}.X"/>
                        </span>
                    </li>
                    <li>
                        <c:if test="${organism == 'HUMAN' && version == 'LATEST'}">
                            <c:url var="url" value="/data/UCSC/human/1/chrY.gff.gz"/>
                        </c:if>
                        <c:if test="${organism == 'HUMAN' && version == 'ARTICLE'}">
                            <c:url var="url" value="/data/UCSC/human/0/chrY.gff.gz"/>
                        </c:if>
                        <c:if test="${organism == 'MOUSE' && version == 'LATEST'}">
                            <c:url var="url" value="/data/UCSC/mouse/1/chrY.gff.gz"/>
                        </c:if>
                        <a href="${url}">
                            <fmt:message key="other.ucsc.chromosome.name.${organism}.${version}">
                                <fmt:param value="Y"/>
                            </fmt:message>
                        </a>
                        <span class="size">
                            <fmt:message key="other.ucsc.chromosome.size.${organism}.${version}.Y"/>
                        </span>
                    </li>
                </ul>   
            </li>
        </ul>
        </fmt:bundle>
    </stripes:layout-component>
</stripes:layout-render>
