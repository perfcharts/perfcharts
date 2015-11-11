NAME=perfcharts
VERSION=0.5.2
DIST_DIR=dist

.PHONY: compile all source tar clean rpm srpm help

help:
	@echo 'make [compile|all|source|tar|clean|rpm|srpm|help]'

compile:
	src/build.sh

all: tar srpm rpm

source:
	mkdir -p $(DIST_DIR)/$(NAME)
	cp -pr src $(DIST_DIR)/$(NAME)
	cp -pr LICENSE $(DIST_DIR)/$(NAME)
	cp -pr README.md $(DIST_DIR)/$(NAME)
	cp -pr perfcharts.spec $(DIST_DIR)/$(NAME)

tar: source
	cd $(DIST_DIR) && tar -czvf $(NAME)-$(VERSION).tar.gz $(NAME)

clean:
	rm -rf $(DIST_DIR)

rpm: tar
	mkdir -p $(DIST_DIR)
	rpmbuild -tb $(DIST_DIR)/$(NAME)-$(VERSION).tar.gz
	cp -f ~/rpmbuild/RPMS/noarch/$(NAME)-$(VERSION)-* $(DIST_DIR)

srpm: tar
	mkdir -p $(DIST_DIR)
	rpmbuild -ts $(DIST_DIR)/$(NAME)-$(VERSION).tar.gz
	cp -f ~/rpmbuild/SRPMS/$(NAME)-$(VERSION)-* $(DIST_DIR)

