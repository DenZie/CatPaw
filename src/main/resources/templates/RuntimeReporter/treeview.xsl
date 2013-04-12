<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<div id="tree1">
			<ul style="display: none;">
				<xsl:apply-templates select="//Suite" mode="treeview"></xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="*" mode="treeview">
	</xsl:template>

	<xsl:template match="Suite|Test|Group" mode="treeview">
		<li class="folder">
			
			<xsl:attribute name="id">
    			<xsl:value-of select="@id"></xsl:value-of>
  			</xsl:attribute>
  			
			<xsl:value-of select="name()"></xsl:value-of>
			:
			<xsl:value-of select="@name"></xsl:value-of>


			<ul>
				<xsl:apply-templates mode="treeview"></xsl:apply-templates>

			</ul>

		</li>
	</xsl:template>


	<xsl:template match="Class" mode="treeview">
		<li>
			<xsl:attribute name="id">
    			<xsl:value-of select="@id"></xsl:value-of>
  			</xsl:attribute>
			<xsl:value-of select="name()"></xsl:value-of>
			:
			<xsl:value-of select="@name"></xsl:value-of>
		</li>
	</xsl:template>



</xsl:stylesheet>