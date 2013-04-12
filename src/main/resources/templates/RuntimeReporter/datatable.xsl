<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output omit-xml-declaration="yes" indent="yes" />



	<xsl:param name="sample"></xsl:param>

	<xsl:template match="/">


		<h1 id='head1' style="width:100%;" class="heading">Configuration Summary</h1>
		<table id='overall1' cellpadding="0" cellspacing="0" border="0"
			class="display" width="100%">
			<tbody>

				<xsl:for-each select="/Runtime_Reporter/Config/child::*">
					<tr>
						<td style="width:200px">
							<xsl:value-of select="name()"></xsl:value-of>

						</td>
						<td>
							<xsl:value-of select="text()"></xsl:value-of>

						</td>
					</tr>

				</xsl:for-each>

			</tbody>
		</table>
		<div>
			<br></br>
		</div>
		<h1 id='head1' style="width:100%;" class="heading">Test Summary</h1>
		<table id='overall' cellpadding="0" cellspacing="0" border="0"
			class="display" width="100%">
			<thead width="100%">
				<tr>
					<th>Type</th>
					<th>Name</th>
					<th>Total</th>
					<th>Passed</th>
					<th>Failed</th>
					<th>Skipped</th>
					<th>Running</th>
				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select=".//*" mode="report"></xsl:apply-templates>
			</tbody>
		</table>
		<div>
			<br></br>
		</div>

		<h1 id='head3' style="width:100%;" class="heading">Test Methods Report</h1>
		<table id="example" cellpadding="0" cellspacing="0" border="0"
			class="display">
			<thead>
				<tr>
					<th></th>
					<th>Suite</th>
					<th>Test</th>
					<th>Group</th>
					<th>Class</th>
					<th>Method</th>
					<th>Parameters</th>
					<th>Status</th>
					<th>Start Time</th>
					<th>End Time</th>
					<th>Exception</th>
					<th>Stack Trace</th>
					<th>Logs</th>

				</tr>
			</thead>
			<tbody id='tbody'>
				<xsl:apply-templates select="//*" mode="report-finder" />
			</tbody>
		</table>

		<div>
			<br></br>
		</div>

		<h1 id='head2' style="width:100%;" class="heading">Configuration Methods Report</h1>
		<table id='config' cellpadding="0" cellspacing="0" border="0"
			class="display">
			<thead>
				<tr>
					<th></th>
					<th>Name</th>
					<th>Class Name</th>
					<th>Type</th>
					<th>Parameters</th>
					<th>Start Time</th>
					<th>End Time</th>
					<th>Status</th>
					<th>Description</th>
					<th>Exception</th>
					<th>Stack Trace</th>
					<th>Logs</th>


				</tr>
			</thead>
			<tbody>
				<xsl:apply-templates select="//*"
					mode="configuration-finder"></xsl:apply-templates>
			</tbody>
		</table>


	</xsl:template>

	<xsl:template match="*" mode="report-finder">
		<xsl:if test="@id=$sample">
			<xsl:apply-templates select=".//Test_Method"></xsl:apply-templates>

		</xsl:if>

	</xsl:template>

	<xsl:template match="*" mode="configuration-finder">
		<xsl:if test="@id=$sample">
			<xsl:apply-templates select=".//Config" mode="configuration"></xsl:apply-templates>

		</xsl:if>

	</xsl:template>

	<xsl:template match="*" mode="report">
		<xsl:if test="@id=$sample">
			<tr class="odd">
				<td class="center">
					<xsl:value-of select="name()"></xsl:value-of>
				</td>
				<td class="center">
					<xsl:value-of select="@name"></xsl:value-of>
				</td>
				<td class="center">
					<xsl:value-of select="@Total"></xsl:value-of>
				</td>
				<td class="center">
					<xsl:value-of select="@Passed"></xsl:value-of>
				</td>
				<td class="center">
					<xsl:value-of select="@Failed"></xsl:value-of>
				</td>
				<td class="center">
					<xsl:value-of select="@Skipped"></xsl:value-of>
				</td>
				<td class="center">
					<xsl:value-of select="@Running"></xsl:value-of>
				</td>
			</tr>

		</xsl:if>

	</xsl:template>



	<xsl:template match="Config" mode="configuration">
		<tr>
			<td class="control center">
				<img src='resources/details_open.png' />
			</td>
			<td>
				<xsl:value-of select="@name"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="@classname"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="@type"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="@Parameters"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="@Start_Time"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="@End_Time"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="@Status"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="@Description"></xsl:value-of>
			</td>
			<td>
				<xsl:value-of select="./Exception/text()"></xsl:value-of>
			</td>

			<td>
				<xsl:for-each select="./StackTrace/child::*">

					<xsl:value-of select="text()"></xsl:value-of>
					<br></br>

				</xsl:for-each>
			</td>
			<td>
				<table width="100%">
					<xsl:for-each select="./Logs/child::*">
						<tr>
							<td>
								<xsl:value-of select="text()" />
							</td>
							<td style='width: 100px'>

								<xsl:if test="@screenshot">
									<a>
										<xsl:attribute name="href">
					       <xsl:value-of select="@screenshot" /></xsl:attribute>
										Screen Shot
									</a>
								</xsl:if>
							</td>
							<td style='width: 100px'>

								<xsl:if test="@source">
									<a>
										<xsl:attribute name="href">
					       <xsl:value-of select="@source" /></xsl:attribute>
										Source
									</a>
								</xsl:if>
							</td>
						</tr>

					</xsl:for-each>
				</table>
			</td>

		</tr>
	</xsl:template>


	<xsl:template match="Test_Method">
		<tr>
			<td class="control center">
				<img src='resources/details_open.png' />
			</td>
			<xsl:if test="name(../..)='Group'">
				<td>
					<xsl:value-of select="../../../../@name"></xsl:value-of>
				</td>
				<td>
					<xsl:value-of select="../../../@name"></xsl:value-of>
				</td>
				<td>
					<xsl:value-of select="../../@name"></xsl:value-of>
				</td>

			</xsl:if>

			<xsl:if test="name(../..)='Test'">
				<td>
					<xsl:value-of select="../../../@name"></xsl:value-of>
				</td>
				<td>
					<xsl:value-of select="../../@name"></xsl:value-of>
				</td>
				<td>
				</td>

			</xsl:if>


			<xsl:if test="name(..)">
				<td>
					<xsl:value-of select="../@name"></xsl:value-of>
				</td>

			</xsl:if>

			<xsl:if test="name(.)">

				<td>
					<xsl:value-of select="@name"></xsl:value-of>
				</td>
				<td>
					<xsl:value-of select="@Parameters"></xsl:value-of>
				</td>
				<td>
					<xsl:value-of select="@Status"></xsl:value-of>
				</td>
				<td>
					<xsl:value-of select="@Start_Time"></xsl:value-of>
				</td>
				<td>
					<xsl:value-of select="@End_Time"></xsl:value-of>
				</td>
				<td>
					<xsl:value-of select="@Description"></xsl:value-of>
				</td>

				<td>
					<xsl:value-of select="./Exception/text()"></xsl:value-of>
				</td>

				<td>
					<xsl:for-each select="./StackTrace/child::*">

						<xsl:value-of select="text()"></xsl:value-of>
						<br></br>

					</xsl:for-each>
				</td>

				<td>
					<table width="100%">
						<xsl:for-each select="./Logs/child::*">
							<tr>
								<td>
									<xsl:value-of select="text()" />
								</td>
								<td style='width: 100px'>

									<xsl:if test="@screenshot">
										<a class="fancybox" data-fancybox-group="gallery">
											<xsl:attribute name="href">
					       <xsl:value-of select="@screenshot" /></xsl:attribute>
											Screen Shot
										</a>
									</xsl:if>
								</td>
								<td style='width: 100px'>

									<xsl:if test="@source">
										<a>
											<xsl:attribute name="href">
					       <xsl:value-of select="@source" /></xsl:attribute>
											Source
										</a>
									</xsl:if>
								</td>
							</tr>

						</xsl:for-each>
					</table>
				</td>


			</xsl:if>
		</tr>

	</xsl:template>


</xsl:stylesheet>

