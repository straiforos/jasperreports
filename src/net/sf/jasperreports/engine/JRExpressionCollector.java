/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2005 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 185, Berry Street, Suite 6200
 * San Francisco CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.engine;

import java.util.Collection;
import java.util.HashSet;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class JRExpressionCollector
{

	
	/**
	 *
	 */
	private Collection expressions = new HashSet();


	/**
	 *
	 */
	private void addExpression(JRExpression expression)
	{
		if (expression != null)
		{
			expressions.add(expression);
		}
	}

	/**
	 *
	 */
	public Collection collect(JRReport report)
	{
		collect(report.getParameters());
		collect(report.getVariables());
		collect(report.getGroups());

		collect(report.getBackground());
		collect(report.getTitle());
		collect(report.getPageHeader());
		collect(report.getColumnHeader());
		collect(report.getDetail());
		collect(report.getColumnFooter());
		collect(report.getPageFooter());
		collect(report.getLastPageFooter());
		collect(report.getSummary());
		
		return expressions;
	}
		

	/**
	 *
	 */
	private void collect(JRParameter[] parameters)
	{
		if (parameters != null && parameters.length > 0)
		{
			for(int i = 0; i < parameters.length; i++)
			{
				addExpression(parameters[i].getDefaultValueExpression());
			}
		}
	}


	/**
	 *
	 */
	private void collect(JRVariable[] variables)
	{
		if (variables != null && variables.length > 0)
		{
			for(int i = 0; i < variables.length; i++)
			{
				JRVariable variable = variables[i];
				addExpression(variable.getExpression());
				addExpression(variable.getInitialValueExpression());
			}
		}
	}


	/**
	 *
	 */
	private void collect(JRGroup[] groups)
	{
		if (groups != null && groups.length > 0)
		{
			for(int i = 0; i < groups.length; i++)
			{
				JRGroup group = groups[i];
				addExpression(group.getExpression());

				collect(group.getGroupHeader());
				collect(group.getGroupFooter());
			}
		}
	}


	/**
	 *
	 */
	private void collect(JRBand band)
	{
		if (band != null)
		{
			addExpression(band.getPrintWhenExpression());
	
			JRElement[] elements = band.getElements();
			if (elements != null && elements.length > 0)
			{
				for(int i = 0; i < elements.length; i++)
				{
					elements[i].collectExpressions(this);
				}
			}
		}
	}

	/**
	 *
	 */
	private void collectElement(JRElement element)
	{
		addExpression(element.getPrintWhenExpression());
	}

	/**
	 *
	 */
	private void collectAnchor(JRAnchor anchor)
	{
		addExpression(anchor.getAnchorNameExpression());
	}

	/**
	 *
	 */
	private void collectHyperlink(JRHyperlink hyperlink)
	{
		addExpression(hyperlink.getHyperlinkReferenceExpression());
		addExpression(hyperlink.getHyperlinkAnchorExpression());
		addExpression(hyperlink.getHyperlinkPageExpression());
	}

	/**
	 *
	 */
	public void collect(JRLine line)
	{
		collectElement(line);
	}

	/**
	 *
	 */
	public void collect(JRRectangle rectangle)
	{
		collectElement(rectangle);
	}

	/**
	 *
	 */
	public void collect(JREllipse ellipse)
	{
		collectElement(ellipse);
	}

	/**
	 *
	 */
	public void collect(JRImage image)
	{
		collectElement(image);
		addExpression(image.getExpression());
		collectAnchor(image);
		collectHyperlink(image);
	}

	/**
	 *
	 */
	public void collect(JRStaticText staticText)
	{
		collectElement(staticText);
	}

	/**
	 *
	 */
	public void collect(JRTextField textField)
	{
		collectElement(textField);
		addExpression(textField.getExpression());
		collectAnchor(textField);
		collectHyperlink(textField);
	}

	/**
	 *
	 */
	public void collect(JRSubreport subreport)
	{
		collectElement(subreport);
		addExpression(subreport.getParametersMapExpression());

		JRSubreportParameter[] parameters = subreport.getParameters();
		if (parameters != null && parameters.length > 0)
		{
			for(int j = 0; j < parameters.length; j++)
			{
				addExpression(parameters[j].getExpression());
			}
		}

		addExpression(subreport.getConnectionExpression());
		addExpression(subreport.getDataSourceExpression());
		addExpression(subreport.getExpression());
	}

	/**
	 *
	 */
	private void collectChart(JRChart chart)
	{
		collectElement(chart);
		collectAnchor(chart);
		collectHyperlink(chart);
		
		addExpression(chart.getTitleExpression());
		addExpression(chart.getSubtitleExpression());
	}

	/**
	 *
	 */
	public void collect(JRPieChart pieChart)
	{
		collectChart(pieChart);
		collect((JRPieDataset)pieChart.getDataset());
	}

	/**
	 *
	 */
	private void collect(JRPieDataset pieDataset)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(pieDataset.getKeyExpression());
		addExpression(pieDataset.getValueExpression());
		addExpression(pieDataset.getLabelExpression());
	}

	/**
	 *
	 */
	private void collect(JRPiePlot piePlot)
	{
	}

	/**
	 *
	 */
	public void collect(JRPie3DChart pie3DChart)
	{
		collectElement(pie3DChart);
		collectAnchor(pie3DChart);
		collectHyperlink(pie3DChart);
		collectChart(pie3DChart);
		collect((JRPieDataset)pie3DChart.getDataset());
	}

	/**
	 *
	 */
	public void collect(JRBarChart barChart)
	{
		collectChart(barChart);
		collect((JRCategoryDataset)barChart.getDataset());
	}

	/**
	 *
	 */
	private void collect(JRCategoryDataset categoryDataset)//FIXME NOW JRChartDataset should have collect like all elements?
	{
		addExpression(categoryDataset.getCategoryExpression());
		addExpression(categoryDataset.getValueExpression());
		addExpression(categoryDataset.getLabelExpression());
	}

	/**
	 *
	 */
	private void collect(JRBarPlot barPlot)//FIXME NOW JRChartDataset should have collect like all elements?
	{
	}

}
