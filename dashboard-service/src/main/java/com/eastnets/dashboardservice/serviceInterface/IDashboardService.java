package com.eastnets.dashboardservice.serviceInterface;

import org.primefaces.model.chart.PieChartModel;

public interface IDashboardService {
    PieChartModel createPieModel();
    String getAverageTalkTime();
    String getLongestTalkTime();
    int getTotalCalls();
}
