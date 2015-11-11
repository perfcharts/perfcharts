Name:           perfcharts
Version:        0.5.2
Release:        1%{?dist}
Summary:        Perfcharts is a tool for generating performance testing reports
BuildArch:	noarch

License:        GNU AFFERO GENERAL PUBLIC LICENSE
URL:            https://docs.engineering.redhat.com/x/hRfXAQ
Source0:        %{name}-%{version}.tar.gz

BuildRequires:  java-1.8.0-openjdk-devel
BuildRequires:  ant
Requires:       java-1.8.0-openjdk

%description
Perfcharts is a free software written in Java, which reads performance testing and system monitoring results from Jmeter, NMON, and/or other applications to produce charts for further analysis. It can generate any line and bar chart from any kind of data with appropriate extensions, but now is specially designed for performance testing business.

With this tool, you can get analysis charts by just putting a Jmeter result file (.jtl), some NMON resource monitoring logs (.nmon), and CPU load monitoring logs (.load) into a directory then just running the tool. This tool make it possible to enable automatic performance testing.


%prep
%setup -q -n %{name}


%build
src/build.sh


%install
rm -rf $RPM_BUILD_ROOT
mkdir -p %{buildroot}%{_datadir}/%{name}/bin
cp -p bin/* %{buildroot}%{_datadir}/%{name}/bin
mkdir -p %{buildroot}%{_datadir}/%{name}/res
cp -pr res/* %{buildroot}%{_datadir}/%{name}/res
mkdir -p %{buildroot}%{_datadir}/%{name}/lib
cp -p lib/* %{buildroot}%{_datadir}/%{name}/lib
mkdir -p %{buildroot}%{_bindir}
%files
%doc README.md
%license LICENSE
%{_datadir}/%{name}/bin/*
%{_datadir}/%{name}/res/*
%{_datadir}/%{name}/lib/*

%post
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-perf %{_bindir}/cgt-perf
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-perf2 %{_bindir}/cgt-perf2
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-cmp %{_bindir}/cgt-cmp
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-js %{_bindir}/cgt-js
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-monoreport %{_bindir}/cgt-monoreport
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-trend %{_bindir}/cgt-trend
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-zabbix-dl %{_bindir}/cgt-zabbix-dl
%{__ln_s} -f %{_datadir}/%{name}/bin/cgt-timestamp %{_bindir}/cgt-timestamp

%preun
if [ "$1" = 0 ]; then
  rm -rf %{_bindir}/cgt-perf
  rm -rf %{_bindir}/cgt-perf2
  rm -rf %{_bindir}/cgt-cmp
  rm -rf %{_bindir}/cgt-js
  rm -rf %{_bindir}/cgt-monoreport
  rm -rf %{_bindir}/cgt-trend
  rm -rf %{_bindir}/cgt-zabbix-dl
  rm -rf %{_bindir}/cgt-timestamp
fi

%changelog
* Wed Nov 11 2015 Rayson Zhu <yuxzhu@redhat.com>
- release v0.5.2
- bugfix: parser crashes under some locale environment

