package lggur.shorturl.ui;

import lggur.shorturl.application.ports.ShortLinkQueryPort;
import lggur.shorturl.core.domain.user.UserRepository;
import lggur.shorturl.application.usecase.CreateShortLinkUseCase;
import lggur.shorturl.application.usecase.DeleteShortLinkUseCase;
import lggur.shorturl.application.usecase.EditShortLinkUseCase;
import lggur.shorturl.application.usecase.GetUserStatisticsUseCase;
import lggur.shorturl.application.usecase.RedirectUseCase;
import lggur.shorturl.core.domain.user.User;
import lggur.shorturl.ui.input.Reader;
import lggur.shorturl.ui.output.Printer;

import java.util.UUID;

public class ConsoleContext {

    private User currentUser;

    private final CreateShortLinkUseCase createUseCase;
    private final RedirectUseCase redirectUseCase;
    private final GetUserStatisticsUseCase statisticsUseCase;
    private final EditShortLinkUseCase editUseCase;
    private final DeleteShortLinkUseCase deleteUseCase;
    private final ShortLinkQueryPort queryPort;
    private final UserRepository userRepository;
    private final Printer printer;
    private final Reader reader;

    private final lggur.shorturl.infra.persistence.DataPersistenceService dataPersistenceService;
    private final lggur.shorturl.infra.repository.InMemoryUserRepository inMemoryUserRepository;
    private final lggur.shorturl.infra.repository.InMemoryShortLinkRepository inMemoryLinkRepository;

    public ConsoleContext(
            CreateShortLinkUseCase createUseCase,
            RedirectUseCase redirectUseCase,
            GetUserStatisticsUseCase statisticsUseCase,
            EditShortLinkUseCase editUseCase,
            DeleteShortLinkUseCase deleteUseCase,
            ShortLinkQueryPort queryPort,
            UserRepository userRepository,
            Printer printer,
            Reader reader,
            lggur.shorturl.infra.persistence.DataPersistenceService dataPersistenceService,
            lggur.shorturl.infra.repository.InMemoryUserRepository inMemoryUserRepository,
            lggur.shorturl.infra.repository.InMemoryShortLinkRepository inMemoryLinkRepository) {
        this.createUseCase = createUseCase;
        this.redirectUseCase = redirectUseCase;
        this.statisticsUseCase = statisticsUseCase;
        this.editUseCase = editUseCase;
        this.deleteUseCase = deleteUseCase;
        this.queryPort = queryPort;
        this.userRepository = userRepository;
        this.printer = printer;
        this.reader = reader;
        this.dataPersistenceService = dataPersistenceService;
        this.inMemoryUserRepository = inMemoryUserRepository;
        this.inMemoryLinkRepository = inMemoryLinkRepository;
    }

    public CreateShortLinkUseCase getCreateUseCase() {
        return createUseCase;
    }

    public RedirectUseCase getRedirectUseCase() {
        return redirectUseCase;
    }

    public GetUserStatisticsUseCase getStatisticsUseCase() {
        return statisticsUseCase;
    }

    public EditShortLinkUseCase getEditUseCase() {
        return editUseCase;
    }

    public DeleteShortLinkUseCase getDeleteUseCase() {
        return deleteUseCase;
    }

    public ShortLinkQueryPort getQueryPort() {
        return queryPort;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public Printer getPrinter() {
        return printer;
    }

    public Reader getReader() {
        return reader;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public boolean hasCurrentUser() {
        return currentUser != null;
    }

    public UUID getCurrentUserId() {
        return hasCurrentUser() ? currentUser.getId() : null;
    }

    public lggur.shorturl.infra.persistence.DataPersistenceService getDataPersistenceService() {
        return dataPersistenceService;
    }

    public lggur.shorturl.infra.repository.InMemoryUserRepository getInMemoryUserRepository() {
        return inMemoryUserRepository;
    }

    public lggur.shorturl.infra.repository.InMemoryShortLinkRepository getInMemoryLinkRepository() {
        return inMemoryLinkRepository;
    }
}
