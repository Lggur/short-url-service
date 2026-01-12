package lggur.shorturl;

import lggur.shorturl.application.usecase.CreateShortLinkUseCase;
import lggur.shorturl.application.usecase.DeleteShortLinkUseCase;
import lggur.shorturl.application.usecase.EditShortLinkUseCase;
import lggur.shorturl.application.usecase.ExpireLinksUseCase;
import lggur.shorturl.application.usecase.GetUserStatisticsUseCase;
import lggur.shorturl.application.usecase.RedirectUseCase;
import lggur.shorturl.core.domain.link.ShortLinkFactory;
import lggur.shorturl.infra.repository.InMemoryShortLinkQueryAdapter;
import lggur.shorturl.infra.repository.InMemoryShortLinkRepository;
import lggur.shorturl.infra.repository.InMemoryUserRepository;
import lggur.shorturl.infra.scheduler.ExpiredLinksScheduler;
import lggur.shorturl.infra.service.ConsoleNotificationService;
import lggur.shorturl.infra.service.FixedLifetimePolicy;
import lggur.shorturl.infra.service.SimpleShortCodeGenerator;
import lggur.shorturl.infra.service.SystemClock;
import lggur.shorturl.ui.ConsoleApp;
import lggur.shorturl.ui.ConsoleContext;
import lggur.shorturl.ui.input.ConsoleReader;
import lggur.shorturl.ui.output.ConsolePrinter;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Репозитории

        var linkRepo = new InMemoryShortLinkRepository();

        var userRepo = new InMemoryUserRepository();

        // Адаптер

        var queryAdapter = new InMemoryShortLinkQueryAdapter(linkRepo);

        // Конфигурация

        var config = new lggur.shorturl.infra.config.AppConfig();

        // Доменные сервисы

        var codeGenerator = new SimpleShortCodeGenerator();

        var lifetimePolicy = new FixedLifetimePolicy(config.getDefaultLifetime());

        var factory = new ShortLinkFactory(codeGenerator, lifetimePolicy, config.getMinClicks(),
                config.getMaxClicksLimit());

        // Infra сервисы

        var notifications = new ConsoleNotificationService();

        var clock = new SystemClock();

        // Use cases

        var createUseCase = new CreateShortLinkUseCase(userRepo, linkRepo, factory);

        var redirectUseCase = new RedirectUseCase(linkRepo, notifications, clock);

        var expireUseCase = new ExpireLinksUseCase(queryAdapter, linkRepo, notifications, clock);

        var statisticsUseCase = new GetUserStatisticsUseCase(queryAdapter, clock);

        var editUseCase = new EditShortLinkUseCase(linkRepo, config.getMinClicks(), config.getMaxClicksLimit());

        var deleteUseCase = new DeleteShortLinkUseCase(linkRepo);

        // Планировщик

        var scheduler = new ExpiredLinksScheduler(
                expireUseCase,
                config.getSchedulerInitialDelay(),
                config.getSchedulerPeriod(),
                config.getSchedulerTimeUnit()
        );
        scheduler.start();

        // UI

        var printer = new ConsolePrinter();

        var scanner = new Scanner(System.in);

        var reader = new ConsoleReader(scanner, printer);

        // Сохранение в файлы

        var persistenceService = new lggur.shorturl.infra.persistence.DataPersistenceService();

        var context = new ConsoleContext(
                createUseCase,
                redirectUseCase,
                statisticsUseCase,
                editUseCase,
                deleteUseCase,
                queryAdapter,
                userRepo,
                printer,
                reader,
                persistenceService,
                userRepo,
                linkRepo
        );

        var app = new ConsoleApp(context);

        app.run();
    }
}