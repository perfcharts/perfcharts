NAME=perfcharts
VERSION=0.6.1

RPMBUILD_SPEC=perfcharts.spec
DIST_DIR=dist

SRC_DEST_DIR=$(NAME)-$(VERSION)
TARBALL_DEST_NAME=$(SRC_DEST_DIR).tar.gz

.PHONY: help build clean rpm srpm src tarball

build:
	gradle explodedDist

help:
	@echo 'make [ build | clean | rpm | srpm | src | tarball ]'

clean:
	gradle clean
	rm -rf $(DIST_DIR) build

src:
	rm -rf $(DIST_DIR)/$(SRC_DEST_DIR)
	mkdir -p $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -prf generator $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -prf perftest $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -prf perftest-parser $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -prf tool-zabbix-downloader $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -pf Makefile $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -pf *.gradle $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -pf $(RPMBUILD_SPEC) $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -pf README.md $(DIST_DIR)/$(SRC_DEST_DIR)
	cp -pf LICENSE $(DIST_DIR)/$(SRC_DEST_DIR)

tarball: src
	cd $(DIST_DIR) && tar -czvf $(TARBALL_DEST_NAME) $(SRC_DEST_DIR)

rpm: tarball
	mkdir -p $(DIST_DIR)
	rpmbuild -tb $(DIST_DIR)/$(TARBALL_DEST_NAME)
	cp -f ~/rpmbuild/RPMS/noarch/$(NAME)-$(VERSION)-* $(DIST_DIR)

srpm: tarball
	mkdir -p $(DIST_DIR)
	rpmbuild -ts $(DIST_DIR)/$(TARBALL_DEST_NAME)
	cp -f ~/rpmbuild/SRPMS/$(NAME)-$(VERSION)-* $(DIST_DIR)

