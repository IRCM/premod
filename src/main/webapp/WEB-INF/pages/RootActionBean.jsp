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
        PReMod
    </stripes:layout-component>
    <stripes:layout-component name="head">
        <meta name="robots" content="ALL"/>
        <style>
            div.layout_content div {margin:1em 0;}
            h1 {margin:2em 0;}
            h2 {margin:2em 0 1em 0;}
            p {text-indent:3em; margin:1em 0; padding:0;}
            p.publication {text-indent:0;}
            p.publication a.icon {float:right;}
            div.sample_output {position:relative; height:20em;}
            span.random {position:absolute; top:5em; left:0; width:13em;}
            span.search {position:absolute; top:1em; left:14em; width:29em;}
            span.search img {width:27em; height:16em;}
        </style>
    </stripes:layout-component>
    <stripes:layout-component name="navbar">home</stripes:layout-component>
    <stripes:layout-component name="contents">
        <h1>
            Welcome to PReMod database
        </h1>
        
        <stripes:messages/>
        <stripes:errors/>
        
        <h2>Introduction</h2>
        
        <p>
            The PReMod database describes more than 100,000 computational predicted transcriptional
            regulatory modules within the human genome<sup>1</sup>.
            These modules represent the regulatory potential for 229 transcription factors families
            and are the first genome-wide/transcription factor-wide collection of predicted regulatory
            modules for the human genome<sup>2</sup>.
        </p>
        
        <p>
            The algorithm used involves two steps: (i) Identification and scoring of putative
            transcription factor binding sites using 481 TRANSFAC 7.2 PWMs for vertebrate transcription
            factors. To this end, each non-coding position of the human genome was evaluated for its
            similarity to each PWM using a log-likelihood ratio score with a local GC-parameterized
            third-order Markov background model. Corresponding orthologous positions in mouse and rat
            genomes were evaluated similarly and a weighted average of the human, mouse, and rat
            log-likelihood scores at aligned positions (based on a Multiz (Blanchette et al. 2004)
            genome-wide alignment of these three species) was used to define the matrix score for each
            genomic position and each PWM. (ii) Detection of clustered putative binding sites. To assign
            a "module score" to a given region, the five transcription factors with the highest total
            scoring hits are identified, and a p-value is assigned to the total score observed of the
            top 1, 2, 3, 4, or 5 factors. The p-value computation takes into consideration the number
            of factors involved (1 to 5), their total binding site scores, and the length and GC content
            of the region under evaluation<sup>2</sup>.
        </p>
        
        <p>
            Queries of the database allow a user to specify how much information they want to see.
            For instance, one can retrieve all information for a given region, a given PWM, a given gene
            and so on. Several options are given for textual output or visualization of the data<sup>1</sup>.
        </p>
        
        <h2>Publication</h2>
        
        <p class="publication">
            <c:url var="url" value="/data/publication/NAR_2006.pdf"/>
            <c:url var="image" value="/img/welcome/pdf_logo.jpg"/>
            <a href="${url}" class="icon">
                <img src="${image}" alt="pdf" height="20" width="20"/>
            </a>
            <sup>1</sup>
            Vincent Ferretti, Christian Poitras, Dominique Bergeron, Benoit Coulombe, Fran�ois Robert
            and Mathieu Blanchette*
        </p>
        
        <p class="publication">
            <c:url var="url" value="/data/publication/Blanchette_2006.pdf"/>
            <c:url var="image" value="/img/welcome/pdf_logo.jpg"/>
            <a href="${url}" class="icon">
                <img src="${image}" alt="pdf" height="20" width="20"/>
            </a>
            <sup>2</sup>
            Mathieu Blanchette*, Alain R. Bataille, Xiaoyu Chen, Christian Poitras, Jos�e Lagani�re,
            C�line Lef�bvre, Genevi�ve Deblois, Vincent Gigu�re, Vincent Ferretti, Dominique Bergeron,
            Benoit Coulombe, and Fran�ois Robert*
        </p>
        
        <p class="publication">
            <c:url var="url" value="http://www.mcb.mcgill.ca/~blanchem/GR_module_supp"/>
            <a href="${url}">
                Supplementary material
            </a>
        </p>
        
        <h2>Sample output</h2>
        
        <div class="sample_output">
            <span class="random">
                <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.ReadRandomModuleActionBean"/>
                <a href="${url}">
                    Show a random module<br>(module characteristic)
                </a>
            </span>
            <span class="search">
                <stripes:url var="url" beanclass="ca.mcgill.genomequebec.premod.web.module.SearchModuleActionBean"/>
                <a href="${url}">
                    <c:url var="image" value="/img/welcome/module_sample.png"/>
                    <img src="${image}" alt="Module example" width="300" height="174"/>
                </a>
                <br>
                (click image to go to search page)
            </span>
        </div>
    </stripes:layout-component>
</stripes:layout-render>
