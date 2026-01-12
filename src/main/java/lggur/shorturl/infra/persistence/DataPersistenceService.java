package lggur.shorturl.infra.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lggur.shorturl.core.domain.link.ShortLink;
import lggur.shorturl.core.domain.user.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class DataPersistenceService {

    private static final String DEFAULT_DATA_DIR = "data";
    private static final String LINKS_SUBDIR = "links";
    private static final String USERS_FILE = "users.json";
    private static final String LINKS_FILE_PREFIX = "links_";

    private final String dataDir;
    private final String linksDir;
    private final ObjectMapper objectMapper;

    public DataPersistenceService() {
        this(DEFAULT_DATA_DIR);
    }

    public DataPersistenceService(String baseDataDir) {
        this.dataDir = baseDataDir;
        this.linksDir = baseDataDir + File.separator + LINKS_SUBDIR;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    private void ensureDataDirectoryExists() throws IOException {
        Path dataPath = Paths.get(dataDir);
        if (!Files.exists(dataPath)) {
            Files.createDirectory(dataPath);
        }

        Path linksPath = Paths.get(linksDir);
        if (!Files.exists(linksPath)) {
            Files.createDirectory(linksPath);
        }
    }

    public void saveData(List<User> users, List<ShortLink> links) throws IOException {
        ensureDataDirectoryExists();

        List<UserData> userData = users.stream()
                .map(UserData::fromUser)
                .collect(Collectors.toList());

        File usersFile = new File(dataDir, USERS_FILE);
        objectMapper.writeValue(usersFile, userData);

        Map<UUID, List<ShortLink>> linksByOwner = links.stream()
                .collect(Collectors.groupingBy(ShortLink::getOwnerId));

        for (Map.Entry<UUID, List<ShortLink>> entry : linksByOwner.entrySet()) {
            UUID ownerId = entry.getKey();
            List<ShortLink> userLinks = entry.getValue();

            List<ShortLinkData> linkData = userLinks.stream()
                    .map(ShortLinkData::fromShortLink)
                    .collect(Collectors.toList());

            String filename = LINKS_FILE_PREFIX + ownerId + ".json";
            File linksFile = new File(linksDir, filename);
            objectMapper.writeValue(linksFile, linkData);
        }
    }

    public List<User> loadUsers() throws IOException {
        File usersFile = new File(dataDir, USERS_FILE);
        if (!usersFile.exists()) {
            return new ArrayList<>();
        }

        UserData[] userData = objectMapper.readValue(usersFile, UserData[].class);
        List<User> users = new ArrayList<>();
        for (UserData data : userData) {
            users.add(data.toUser());
        }
        return users;
    }

    public List<ShortLink> loadLinks() throws IOException {
        File linksDirFile = new File(linksDir);
        if (!linksDirFile.exists()) {
            return new ArrayList<>();
        }

        List<ShortLink> allLinks = new ArrayList<>();

        File[] linkFiles = linksDirFile
                .listFiles((dir, name) -> name.startsWith(LINKS_FILE_PREFIX) && name.endsWith(".json"));

        if (linkFiles != null) {
            for (File linkFile : linkFiles) {
                ShortLinkData[] linkData = objectMapper.readValue(linkFile, ShortLinkData[].class);
                for (ShortLinkData data : linkData) {
                    allLinks.add(data.toShortLink());
                }
            }
        }

        return allLinks;
    }
}
