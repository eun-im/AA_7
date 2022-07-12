package function;

import fitnesse.responders.run.SuiteResponder;
import fitnesse.wiki.*;

public class FitnessExample {
    public String testableHtml(PageData pageData, boolean includeSuiteSetup) throws Exception {
        WikiPage wikiPage = pageData.getWikiPage();
        StringBuffer buffer = new StringBuffer();

        if (isTest(pageData)) {
            if (includeSuiteSetup) {
                appendPage(wikiPage, buffer, SuiteResponder.SUITE_SETUP_NAME);
            }
            appendPage(wikiPage, buffer, "SetUp");
        }

        buffer.append(pageData.getContent());
        if (isTest(pageData)) {
            appendPage(wikiPage, buffer, "TearDown");
            if (includeSuiteSetup) {
                appendPage(wikiPage, buffer, SuiteResponder.SUITE_TEARDOWN_NAME);
            }
        }
        pageData.setContent(buffer.toString());
        return pageData.getHtml();
    }

    private void appendPage(WikiPage wikiPage, StringBuffer buffer, String inheritedPage) {
        WikiPage inheritedWikiPage = getInheritedPage(wikiPage, inheritedPage) != null ? getInheritedPage(wikiPage, inheritedPage) : new NullWikiPage();
        setPathName(wikiPage, buffer, pagePath, true);
    }

    private boolean isTest(PageData pageData) {
        return pageData.hasAttribute("Test");
    }

    private WikiPage getInheritedPage(WikiPage wikiPage, String str) {
        return PageCrawlerImpl.getInheritedPage(str, wikiPage);
    }

    private void setPathName(WikiPage wikiPage, StringBuffer buffer, WikiPage wikiPageItem, boolean isSetup) {
        String str = "!include " + (isSetup ? "-setup ." : "-teardown .");
        WikiPagePath pagePath = wikiPage.getPageCrawler().getFullPath(wikiPageItem);
        String pagePathName = PathParser.render(pagePath);
        buffer.append(str).append(pagePathName).append("\n");
    }
}
