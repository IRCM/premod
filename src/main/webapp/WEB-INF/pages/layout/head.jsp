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

<meta name="keywords" content="PReMod, database, module, IRCM, McGill, Genome Quebec, Genome Canada">
<meta name="description" content="Genome-wide computational prediction database of transcriptional regulatory modules.">

<!-- Javascript -->
<c:url var="link" value="/javascript/javascript.js"/>
<script type="text/javascript" src="${link}"></script>
<c:url var="link" value="/javascript/jquery/jquery-1.2.6.min.js"/>
<script type="text/javascript" src="${link}"></script>
<c:url var="link" value="/javascript/jquery.ircm.js"/>
<script type="text/javascript" src="${link}"></script>
<c:url var="link" value="/javascript/jquery/superfish/superfish-1.4.8.js"/>
<script type="text/javascript" src="${link}"></script>

<!-- CSS -->
<c:url var="css" value="/stylesheet/premod_2.0.4.css"/>
<link href="${css}" rel="stylesheet" type="text/css">
<c:url var="css" value="/stylesheet/premod_layout_2.0.css"/>
<link media="screen" href="${css}" rel="stylesheet" type="text/css">
<c:url var="css" value="/stylesheet/premod_layout_2.0_print.css"/>
<link media="print" href="${css}" rel="stylesheet" type="text/css">
<c:url var="link" value="/javascript/jquery/superfish/superfish.css"/>
<link rel="stylesheet" href="${link}" type="text/css">

<!-- Menu -->
<script type="text/javascript">
    jQuery(function(){
        jQuery('ul.sf-menu').superfish({autoArrows: false});
    });
</script>
