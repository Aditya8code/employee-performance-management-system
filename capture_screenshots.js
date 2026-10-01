const puppeteer = require('puppeteer-core');
const path = require('path');
const fs = require('fs');

const CHROME_PATH = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome';
const BASE_URL = 'http://localhost:8080/eps';
const OUTPUT_DIR = path.join(__dirname, 'docs', 'screenshots');

async function run() {
    if (!fs.existsSync(OUTPUT_DIR)) {
        fs.mkdirSync(OUTPUT_DIR, { recursive: true });
    }

    console.log('Launching headless browser via Chrome binary:', CHROME_PATH);
    const browser = await puppeteer.launch({
        executablePath: CHROME_PATH,
        headless: 'new',
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox',
            '--disable-dev-shm-usage',
            '--disable-gpu',
            '--window-size=1440,960'
        ],
        defaultViewport: {
            width: 1440,
            height: 960,
            deviceScaleFactor: 2
        }
    });

    async function snap(page, fileName, waitMs = 1000) {
        if (waitMs > 0) {
            await new Promise(r => setTimeout(r, waitMs));
        }
        const filePath = path.join(OUTPUT_DIR, fileName);
        await page.screenshot({ path: filePath, fullPage: false });
        console.log(`[Captured] ${fileName} (${fs.statSync(filePath).size} bytes)`);
    }

    async function login(page, username, password) {
        await page.goto(`${BASE_URL}/auth/login`, { waitUntil: 'domcontentloaded' });
        await page.waitForSelector('#username');
        await page.$eval('#username', (el, val) => el.value = val, username);
        await page.$eval('#password', (el, val) => el.value = val, password);
        await Promise.all([
            page.waitForNavigation({ waitUntil: 'domcontentloaded' }),
            page.click('button[type="submit"]')
        ]);
    }

    try {
        // --- CONTEXT 1: Anonymous / Login ---
        console.log('\n--- 1. Login Page ---');
        const ctx1 = await browser.createBrowserContext();
        const p1 = await ctx1.newPage();
        await p1.goto(`${BASE_URL}/auth/login`, { waitUntil: 'networkidle0' });
        await snap(p1, '01-login-page.png', 500);
        await ctx1.close();

        // --- CONTEXT 2: Admin ---
        console.log('\n--- 2-5 & 10. Admin Pages ---');
        const adminCtx = await browser.createBrowserContext();
        const adminPage = await adminCtx.newPage();
        await login(adminPage, 'admin', 'Admin@123');
        
        console.log('Capturing 02-admin-dashboard.png...');
        await adminPage.goto(`${BASE_URL}/admin/dashboard`, { waitUntil: 'networkidle0' });
        await snap(adminPage, '02-admin-dashboard.png', 1200);

        console.log('Capturing 03-employee-management.png...');
        await adminPage.goto(`${BASE_URL}/admin/employees`, { waitUntil: 'networkidle0' });
        await snap(adminPage, '03-employee-management.png', 800);

        console.log('Capturing 04-department-management.png...');
        await adminPage.goto(`${BASE_URL}/admin/departments`, { waitUntil: 'networkidle0' });
        await snap(adminPage, '04-department-management.png', 800);

        console.log('Capturing 05-evaluation-cycle.png...');
        await adminPage.goto(`${BASE_URL}/admin/cycles`, { waitUntil: 'networkidle0' });
        await snap(adminPage, '05-evaluation-cycle.png', 800);

        console.log('Capturing 10-reports-page.png...');
        await adminPage.goto(`${BASE_URL}/admin/reports`, { waitUntil: 'networkidle0' });
        await snap(adminPage, '10-reports-page.png', 1500);
        await adminCtx.close();

        // --- CONTEXT 3: Manager ---
        console.log('\n--- 6-7. Manager Pages ---');
        const mgrCtx = await browser.createBrowserContext();
        const mgrPage = await mgrCtx.newPage();
        await login(mgrPage, 'manager1', 'Manager@123');

        console.log('Capturing 06-manager-dashboard.png...');
        await mgrPage.goto(`${BASE_URL}/manager/dashboard`, { waitUntil: 'networkidle0' });
        await snap(mgrPage, '06-manager-dashboard.png', 1000);

        console.log('Capturing 07-employee-evaluation-form.png...');
        await mgrPage.goto(`${BASE_URL}/manager/evaluate?employeeId=3`, { waitUntil: 'networkidle0' });
        await mgrPage.waitForSelector('form');

        const criteriaIds = [1, 2, 3, 4, 5, 6];
        const scores = [5, 4, 5, 4, 5, 4];
        const comments = [
            'Flawless punctuality, always present for team syncs and standups.',
            'Exceptional code quality, clean architecture, and thorough test cases.',
            'Consistently high throughput across complex engineering sprints.',
            'Collaborative mindset, always ready to assist and unblock peers.',
            'Clear and succinct technical communication in PRs and documentation.',
            'Exceeded quarterly engineering OKRs and project deliverables.'
        ];

        for (let i = 0; i < criteriaIds.length; i++) {
            const critId = criteriaIds[i];
            const scoreVal = scores[i];
            const radioSel = `input[name="score_${critId}"][value="${scoreVal}"]`;
            if (await mgrPage.$(radioSel)) {
                await mgrPage.click(radioSel);
            }
            const commentSel = `textarea[name="score_comment_${critId}"]`;
            if (await mgrPage.$(commentSel)) {
                await mgrPage.type(commentSel, comments[i]);
            }
        }

        if (await mgrPage.$('textarea[name="overallFeedback"]')) {
            await mgrPage.type('textarea[name="overallFeedback"]', 'Charlie has delivered an outstanding performance this cycle with exceptional technical impact.');
        }
        if (await mgrPage.$('textarea[name="strengths"]')) {
            await mgrPage.type('textarea[name="strengths"]', 'System architecture, API optimization, problem-solving, and team mentoring.');
        }
        if (await mgrPage.$('textarea[name="improvements"]')) {
            await mgrPage.type('textarea[name="improvements"]', 'Take the lead on external tech sharing and customer-facing incident reviews.');
        }

        await snap(mgrPage, '07-employee-evaluation-form.png', 1000);
        await mgrCtx.close();

        // --- CONTEXT 4: Employee ---
        console.log('\n--- 8-9. Employee Pages ---');
        const empCtx = await browser.createBrowserContext();
        const empPage = await empCtx.newPage();
        await login(empPage, 'emp1', 'Employee@123');

        console.log('Capturing 08-employee-dashboard.png...');
        await empPage.goto(`${BASE_URL}/employee/dashboard`, { waitUntil: 'networkidle0' });
        await snap(empPage, '08-employee-dashboard.png', 1200);

        console.log('Capturing 09-employee-evaluation-result.png...');
        await empPage.goto(`${BASE_URL}/employee/evaluation-details?id=1`, { waitUntil: 'networkidle0' });
        await snap(empPage, '09-employee-evaluation-result.png', 1500);
        await empCtx.close();

        console.log('\n==================================================');
        console.log('  ALL 10 SCREENSHOTS CAPTURED SUCCESSFULLY!');
        console.log('==================================================');

    } catch (err) {
        console.error('Error during screenshot capture:', err);
    } finally {
        await browser.close();
    }
}

run();
