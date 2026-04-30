package base;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class BasePage {
    protected Page page;

    public BasePage(Page page) {
        this.page = page;
    }

    // Common methods that all pages can use
    public void navigateTo(String url) {
        page.navigate(url);
    }

    public void waitForTimeout(int milliseconds) {
        page.waitForTimeout(milliseconds);
    }

    public void clickElement(String selector) {
        page.locator(selector).click();
    }

    public void fillElement(String selector, String text) {
        page.locator(selector).fill(text);
    }

    public String getTextContent(String selector) {
        return page.locator(selector).textContent();
    }

    public void waitForElement(String selector) {
        page.locator(selector).waitFor();
    }

    public boolean isElementVisible(String selector) {
        return page.locator(selector).isVisible();
    }

    public void typeText(String text) {
        page.keyboard().type(text);
    }

    public void pressKey(String key) {
        page.keyboard().press(key);
    }

    public void clickButtonByRole(String text) {
        page.getByRole(AriaRole.BUTTON).getByText(text).click();
    }

    public void waitForButtonByRole(String text) {
        page.getByRole(AriaRole.BUTTON).getByText(text).waitFor();
    }

    public String getPageTitle() {
        return page.title();
    }
}
