package com.mobiloitte.server.apigateway.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class OrderServiceFilter extends ZuulFilter {

	private static final Logger LOGGER = LogManager.getLogger(OrderServiceFilter.class);

	@Override
	public boolean shouldFilter() {
		LOGGER.debug("request: {}", RequestContext.getCurrentContext().getRequest().getRequestURI());
		String requestURI = RequestContext.getCurrentContext().getRequest().getRequestURI();
		return requestURI.contains("order") && !requestURI.contains("exchange-feed");
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		LOGGER.debug("Filtering request: {}", request.getRequestURI());
		String symbol = request.getParameter("symbol");
		if (symbol == null) {
			try {
				@SuppressWarnings("unchecked")
				Map<String, Object> reqMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
				if (reqMap.containsKey("symbol"))
					symbol = (String) reqMap.get("symbol");
			} catch (MismatchedInputException e) {
				LOGGER.debug("Skipping order filter");
			} catch (IOException e) {
				LOGGER.catching(e);
			}
		}
		if (symbol != null) {
			LOGGER.debug("Symbol found: {}", symbol);
			String orderService = String.join("-", "order-service", symbol);
			ctx.put(FilterConstants.SERVICE_ID_KEY, orderService);
			LOGGER.debug("Redirecting to service {}", orderService);
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 10;
	}

}
