package JSWD.Web.repositories.chatSpecific;

import JSWD.Web.model.chatSpecific.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepository extends JpaRepository<Image, Long>{};

